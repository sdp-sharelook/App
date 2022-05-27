package com.github.sdpsharelook.camera

import android.Manifest
import android.content.Context
import android.os.Environment
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
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

    // initialized in takePic()
    private lateinit var onSuccessListener: (String) -> Unit
    private lateinit var onErrorListener: () -> Unit
    private lateinit var context: Context

    private lateinit var path: String
    private val cameraLauncher =
        caller.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) onSuccessListener(path)
            else onErrorListener()
        }


    fun takePic(
        context: Context,
        onSuccessListener: (String) -> Unit,
        onErrorListener: () -> Unit,
    ) {
        this.context = context
        this.onSuccessListener = onSuccessListener
        this.onErrorListener = onErrorListener
        val uri = FileProvider.getUriForFile(context, "camera", createImage())
        permissionManager.grantPermission(context, { cameraLauncher.launch(uri) }, onErrorListener)
    }

    fun takePic(context: Context, onSuccessListener: (String) -> Unit) =
        takePic(context, onSuccessListener, {})

    private fun createImage(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageName = "ShareLook_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageName, ".jpg", storageDir)
        path = image.absolutePath
        return image
    }

}