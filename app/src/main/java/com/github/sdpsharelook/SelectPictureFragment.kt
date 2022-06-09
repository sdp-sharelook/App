package com.github.sdpsharelook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.github.sdpsharelook.camera.Camera
import com.github.sdpsharelook.databinding.FragmentChoosePictureBinding
import com.github.sdpsharelook.onlinePictures.OnlinePicture
import com.github.sdpsharelook.onlinePictures.OnlinePictureFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import java.io.Serializable

@FlowPreview
@AndroidEntryPoint
class SelectPictureFragment:SelectPictureFragmentLift()

@FlowPreview
open class SelectPictureFragmentLift : BottomSheetDialogFragment() {

    private val camera = Camera(this)
    private lateinit var word: String
    private lateinit var language: String

    companion object {
        val LANGUAGE_PARAMETER = "language"
        val WORD_PARAMETER = "word"
        val RESULT_PARAMETER = "word"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentChoosePictureBinding.inflate(inflater, container, false).apply {
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
                    putSerializable(OnlinePictureFragment.CALLBACK_FUNCTION_PARAMETER,
                        { onlinePicture: OnlinePicture ->
                            returnUri(onlinePicture.mediumLink)
                        } as Serializable)
                }
            }.show(parentFragmentManager, null)
        }
    }.root

    private fun returnUri(picture: String?) {
        setFragmentResult(RESULT_PARAMETER, Bundle().apply { putString(RESULT_PARAMETER, picture) })
        dismiss()
    }
}