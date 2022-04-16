package com.github.sdpsharelook.camera

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.ActivityCameraBinding
import com.github.sdpsharelook.databinding.ActivityTextDetectionBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {

    private var currentPath: String? = null
    private var hasPermissions = false
    private lateinit var inputImage: InputImage
    private lateinit var binding: ActivityCameraBinding
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)


    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this as Context)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.button, null)
        val dialog = builder.create()
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonTakePic.setOnClickListener() {
            takePic()
        }

        binding.buttonTraduire.isEnabled = false

        binding.buttonTraduire.setOnClickListener{


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
            val imageView = binding.cameraImageView
            imageView.setImageURI(uri)
            inputImage = InputImage.fromFilePath(this, uri)
            // Once the image is captured recognize text
            recognizer.process(inputImage)
                .addOnSuccessListener(
                    OnSuccessListener<Text?> { texts ->
                        processTextBlock(texts)
                    })
                .addOnFailureListener(
                    OnFailureListener { e -> // Task failed with an exception
                        e.printStackTrace()
                    })
            binding.buttonTraduire.isEnabled = true
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
        Toast.makeText(this, "Please give camera permission to use this feature", Toast.LENGTH_SHORT).show()

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

    private fun processTextBlock(result: Text) {
        if (result.text.isBlank()){
            binding.textData.text = "Aucun Text"
        }else{
            binding.textData.text = result.text
        }
    }
}