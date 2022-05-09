package com.github.sdpsharelook.camera

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.FragmentCameraBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {


    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentCameraBinding? = null
    private var currentPath: String? = null
    private var hasPermissions = false
    private var textDetected = "Aucun Text"

//    @Inject
    var recognizer : TextRecognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
        builder.setPositiveButton(R.string.button, null)
        val dialog = builder.create()
        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonTakePic.setOnClickListener {
            takePic()
        }

        binding.buttonTraduire.isEnabled = false

        binding.buttonTraduire.setOnClickListener{
            translatWord()
        }
    }

    private var tempImageUri: Uri? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            val file = File(currentPath!!)
            val uri = Uri.fromFile(file)
            val imageView = binding.cameraImageView
            imageView.setImageURI(uri)
            // Once the image is captured recognize text
            recognizeText(InputImage.fromFilePath(requireContext(), uri))
        } else {
            showAlert("Error while taking picture")
        }
    }

    private fun recognizeText(image: InputImage) {
        recognizer.process(image)
            .addOnSuccessListener(
                OnSuccessListener<Text?> { texts ->
                    processTextBlock(texts)
                })
            .addOnFailureListener(
                OnFailureListener { e -> // Task failed with an exception
                    e.printStackTrace()
                })
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
        Toast.makeText(requireContext(), "Please give camera permission to use this feature", Toast.LENGTH_SHORT).show()



    private fun takePic() {
        tempImageUri = FileProvider.getUriForFile(requireContext(), "camera", createImage())
        checkPermissions(Manifest.permission.CAMERA)
        if (checkPerms()) {
            cameraLauncher.launch(tempImageUri)
        }
    }

    private fun checkPermissions(permission: String) = when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) -> {

        }
        else -> {
            requestPermissionLauncher.launch(
                permission
            )
        }
    }

    private fun checkPerms(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun createImage(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US).format(Date())
        val imageName = "ShareLook_"+timeStamp+"_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageName, ".jpg", storageDir)
        currentPath = image.absolutePath
        return image
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun processTextBlock(result: Text) {
        if (result.text.isBlank()){
            textDetected = "Aucun Text"
            binding.textData.text = "Aucun Text"
        }else{
            binding.buttonTraduire.isEnabled = true
            textDetected = result.text
            binding.textData.text = result.text
        }
    }

    fun translatWord(){
        val action = CameraFragmentDirections.actionMenuCameraLinkToMenuTranslateLink2(textDetected)
        findNavController().navigate(action)
    }
}