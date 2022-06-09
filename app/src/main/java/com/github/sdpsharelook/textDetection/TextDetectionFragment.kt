package com.github.sdpsharelook.textDetection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.FragmentTextDetectionBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import javax.inject.Inject

class TextDetectionFragment : Fragment() {

    var inputImage: InputImage? = null

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
            inputImage?.let { image ->
                recognizer.process(image)
                    .addOnSuccessListener(this::processTextBlock)
                    .addOnFailureListener(Exception::printStackTrace)
            }
        }
    }

    private fun processTextBlock(result: Text) {
        if (result.text.isBlank()) {
            binding.textData.text = getString(R.string.no_text_detected)
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