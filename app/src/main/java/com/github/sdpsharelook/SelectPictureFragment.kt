package com.github.sdpsharelook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sdpsharelook.camera.Camera
import com.github.sdpsharelook.databinding.FragmentChoosePictureBinding
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.onlinePictures.OnlinePicture
import com.github.sdpsharelook.onlinePictures.OnlinePictureFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable

class SelectPictureFragment : BottomSheetDialogFragment() {
    private val camera = Camera(this)
    private lateinit var word: String
    private lateinit var language: String
    private lateinit var onPictureSelected: (String?) -> Unit

    companion object {
        val CALLBACK_FUNCTION_PARAMETER = "onPictureSelected"
        val LANGUAGE_PARAMETER = "language"
        val WORD_PARAMETER = "word"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentChoosePictureBinding.inflate(inflater, container, false).apply {
        onPictureSelected =
            arguments?.getSerializable(CALLBACK_FUNCTION_PARAMETER) as (String?) -> Unit
        language = arguments?.getString(LANGUAGE_PARAMETER)!!
        word = arguments?.getString(WORD_PARAMETER)!!
        buttonCamera.setOnClickListener {
            camera.takePic(requireContext()) { returnUri(it) }
        }
        buttonClearPicture.setOnClickListener { returnUri(null) }
        buttonWeb.setOnClickListener {
            OnlinePictureFragment(
            ).apply {
                arguments = Bundle().apply {
                    putString(OnlinePictureFragment.WORD_PARAMETER, word)
                    putString(OnlinePictureFragment.LANGUAGE_PARAMETER, language)
                    putSerializable(OnlinePictureFragment.CALLBACK_FUNCTION_PARAMETER, {
                        onlinePicture:OnlinePicture ->
                        returnUri(onlinePicture.mediumLink)
                    } as Serializable)
                }
            }.show(parentFragmentManager, null)
        }
    }.root

    private fun returnUri(picture: String?) {
        dismiss()
        onPictureSelected(picture)
    }
}