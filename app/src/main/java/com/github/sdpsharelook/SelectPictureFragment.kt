package com.github.sdpsharelook

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import com.github.sdpsharelook.camera.Camera
import com.github.sdpsharelook.databinding.FragmentChoosePictureBinding
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.onlinePictures.OnlinePictureFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectPictureFragment(private val word: Word) : BottomSheetDialogFragment() {
    private val camera = Camera(this@SelectPictureFragment).apply {
        setOnErrorListener {
            Toast.makeText(requireContext(),
                "Error taking picture",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentChoosePictureBinding.inflate(inflater, container, false).apply {
        buttonCamera.setOnClickListener {
            camera.takePic(requireContext())
        }
        buttonStorage.setOnClickListener { }
        buttonWeb.setOnClickListener {
            var bitmap: Bitmap? = null
            CoroutineScope(Dispatchers.Main).launch {
                val onlinePic = OnlinePictureFragment(
                    word.source!!,
                    word.sourceLanguage!!
                ).show(parentFragmentManager, null)
            }

        }
    }.root

}