package com.github.sdpsharelook.camera

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.sdpsharelook.R
import com.github.sdpsharelook.Utils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {

    private var currentPath: String? = null
    private var hasPermissions = false

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this as Context)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.button, null)
        val dialog = builder.create()
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        findViewById<Button>(R.id.buttonTakePic).setOnClickListener() {
            takePic()
        }
    }

    fun takePic() {
        tempImageUri = FileProvider.getUriForFile(this, "camera", createImage())
        checkPermissions(Manifest.permission.CAMERA)
        if (checkPerms()) {
            cameraLauncher.launch(tempImageUri)
        }
    }

    private var tempImageUri: Uri? = null
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            val file = File(currentPath)
            val uri = Uri.fromFile(file)
            val imageView = findViewById<ImageView>(R.id.cameraImageView)
            imageView.setImageURI(uri)
        } else {
            showAlert("Error while taking picture")
        }
    }

    private val requestPermissionLauncher =
        this.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                hasPermissions = true
            } else {
                errorPermission()
            }
        }

    private fun errorPermission() =
        Utils.toast("Please give camera permission to use this feature", this)

    fun checkPermissions(permission: String) = when {
        ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED -> {

        }
        else -> {
            requestPermissionLauncher.launch(
                permission
            )
        }
    }
    fun checkPerms(): Boolean {
        val permission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        return permission1
    }

    private fun createImage(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageName = "ShareLook_"+timeStamp+"_"
        var storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image = File.createTempFile(imageName, ".jpg", storageDir)
        currentPath = image.absolutePath
        return image
    }
}