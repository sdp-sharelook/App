package com.github.sdpsharelook.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.sdpsharelook.permissions.PermissionManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Camera(caller: ActivityResultCaller) {


    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val permissionManager = PermissionManager(Manifest.permission.CAMERA, caller)
    private var currentPath: String? = null
    private var hasPermissions = false
    var tempImageUri: Uri? = null
    private lateinit var context: Context
    private val cameraLauncher =
        caller.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val file = File(currentPath!!)
                val uri = Uri.fromFile(file)
                // todo use uri and do something
            }
        }


    private var onErrorListener = {}
    private fun errorPermission() =
        onErrorListener()

    fun setOnErrorListener(listener: () -> Unit) {
        onErrorListener = listener
    }


    fun takePic(context: Context) {
        this.context = context
        tempImageUri = FileProvider.getUriForFile(context, "camera", createImage())
        permissionManager.grantPermission(context) { cameraLauncher.launch(tempImageUri) }
    }


    private fun createImage(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageName = "ShareLook_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageName, ".jpg", storageDir)
        currentPath = image.absolutePath
        return image
    }

}