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
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import javax.inject.Inject

class TextDetectionFragment : Fragment() {

    private lateinit var inputImage: InputImage
    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentTextDetectionBinding? = null

    @Inject
    lateinit var recognizer : TextRecognizer


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun processTextBlock(result: Text) {
        if (result.text.isBlank()) {
            binding.textData.text = "Aucun Text"
        } else {
            binding.textData.text = result.text
        }
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