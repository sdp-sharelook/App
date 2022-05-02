package com.github.sdpsharelook.textDetection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.FragmentTextDetectionBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextDetectionFragment : Fragment() {

    private lateinit var inputImage: InputImage
    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentTextDetectionBinding? = null
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detectButton.isEnabled = false

// This is when we will use the camera
//        inputImage = imageFromMediaImage(ImageProxy.image, 0 )
        binding.captureButton.setOnClickListener{
//            dispatchTakePictureIntent()
            val image = binding.imageView.drawToBitmap()
            inputImage = InputImage.fromBitmap(image, 0)
            binding.detectButton.isEnabled = true
            binding.textData.text = "Detect text on the image"
        }

        binding.detectButton.setOnClickListener{

            recognizer.process(inputImage)
                .addOnSuccessListener(
                    OnSuccessListener<Text?> { texts ->
                        processTextBlock(texts)
                    })
                .addOnFailureListener(
                    OnFailureListener { e -> // Task failed with an exception
                        e.printStackTrace()
                    })
        }
    }
    /** All the comment code will maybe usefull to capture photo with the camera*/
//    val REQUEST_IMAGE_CAPTURE = 1

//    private fun dispatchTakePictureIntent() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//        } catch (e: ActivityNotFoundException) {
//            // display error state to the user
//            binding.textData.text = e.toString()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            val image = data?.extras?.get("data") as Bitmap
//            inputImage = InputImage.fromBitmap(image, 0)
//            binding.imageView.setImageBitmap(image)
//            binding.detectButton.isEnabled = true
//            binding.textData.text = "Detect text on the image"
//        }
//    }
//
//    private fun imageFromMediaImage(mediaImage: Image, rotation: Int) :InputImage {
//        return InputImage.fromMediaImage(mediaImage, rotation)
//    }

    private fun processTextBlock(result: Text) {
        if (result.text.isBlank()) {
            binding.textData.text = "Aucun Text"
        } else {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextDetectionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}