package com.github.sdpsharelook.textDetection

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.github.sdpsharelook.databinding.ActivityTextDetectionBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class TextDetectionActivity : AppCompatActivity() {
    private lateinit var inputImage: InputImage
    private lateinit var binding: ActivityTextDetectionBinding
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.detectButton.isEnabled = false

//        inputImage = imageFromMediaImage(ImageProxy.image, 0 )
        binding.captureButton.setOnClickListener{
            dispatchTakePictureIntent()
        }

        binding.detectButton.setOnClickListener{

            recognizer.process(inputImage)
                .addOnSuccessListener(
                    OnSuccessListener<Text?> { texts ->
                        processTextBlock(texts)
                    })
                .addOnFailureListener(
                    OnFailureListener { e -> // Task failed with an exception
                    })
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
            binding.textData.text = e.toString()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val image = data?.extras?.get("data") as Bitmap
            inputImage = InputImage.fromBitmap(image, 0)
            binding.imageView.setImageBitmap(image)
            binding.detectButton.isEnabled = true
            binding.textData.text = "Detect text on the image"
        }
    }

    private fun imageFromMediaImage(mediaImage: Image, rotation: Int) :InputImage {
        return InputImage.fromMediaImage(mediaImage, rotation)
    }

    private fun processTextBlock(result: Text) {
        if (result.text.isBlank()){
            binding.textData.text = "Aucun Text"
        }else{
            binding.textData.text = result.text
        }
//        for (block in result.textBlocks) {
//            val blockText = block.text
//            val blockCornerPoints = block.cornerPoints
//            val blockFrame = block.boundingBox
//            for (line in block.lines) {
//                val lineText = line.text
//                val lineCornerPoints = line.cornerPoints
//                val lineFrame = line.boundingBox
//                for (element in line.elements) {
//                    val elementText = element.text
//                    val elementCornerPoints = element.cornerPoints
//                    val elementFrame = element.boundingBox
//                }
//            }
//        }
    }




}