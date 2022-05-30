package com.github.sdpsharelook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sdpsharelook.camera.Camera
import com.github.sdpsharelook.databinding.FragmentChoosePictureBinding
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.onlinePictures.OnlinePictureFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectPictureFragment(
    private val word: Word,
    private val onPictureSelected: (String?) -> Unit = {},
) : BottomSheetDialogFragment() {
    private val camera = Camera(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentChoosePictureBinding.inflate(inflater, container, false).apply {
        buttonCamera.setOnClickListener {
            camera.takePic(requireContext()) { returnUri(it) }
        }
        buttonClearPicture.setOnClickListener { returnUri(null) }
        buttonWeb.setOnClickListener {
            word.source?.let { it1 ->
                OnlinePictureFragment(
                    it1,
                    Language.auto
                ).show(parentFragmentManager, null) { returnUri(it.mediumLink) }
            }
        }
    }.root

    private fun returnUri(picture: String?) {
        dismiss()
        onPictureSelected(picture)
    }
}