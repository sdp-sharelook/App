package com.github.sdpsharelook.camera

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.FragmentCameraBinding
import com.github.sdpsharelook.databinding.FragmentLoginBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Callable

class Camera(caller: ActivityResultCaller) {


    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private var currentPath: String? = null
    private var hasPermissions = false
    var tempImageUri: Uri? = null

    private val cameraLauncher =
        caller.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val file = File(currentPath!!)
                val uri = Uri.fromFile(file)
                // todo use uri and do something
            }
        }

    private val requestPermissionLauncher =
        caller.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                hasPermissions = true
            } else {
                errorPermission()
            }
        }
    private var onErrorListener = {}
    private fun errorPermission() =
        onErrorListener()

    fun setOnErrorListener(listener: () -> Unit) {
        onErrorListener = listener
    }


    fun takePic(context: Context) {
        tempImageUri = FileProvider.getUriForFile(context, "camera", createImage(context))
        checkPermissions(Manifest.permission.CAMERA, context)
        if (checkPerms(context)) {
            cameraLauncher.launch(tempImageUri)
        }
    }

    private fun checkPermissions(permission: String, context: Context) = when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            context,
            permission
        ),
        -> {

        }
        else -> {
            requestPermissionLauncher.launch(
                permission
            )
        }
    }

    private fun checkPerms(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun createImage(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageName = "ShareLook_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageName, ".jpg", storageDir)
        currentPath = image.absolutePath
        return image
    }

}