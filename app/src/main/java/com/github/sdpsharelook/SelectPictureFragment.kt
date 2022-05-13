package com.github.sdpsharelook

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sdpsharelook.databinding.FragmentChoosePictureBinding
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.onlinePictures.OnlinePictureFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectPictureFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentChoosePictureBinding.inflate(inflater, container, false).apply {
        buttonCamera.setOnClickListener { }
        buttonStorage.setOnClickListener { }
        buttonWeb.setOnClickListener {
            var bitmap: Bitmap? = null
            CoroutineScope(Dispatchers.Main).launch {
                val onlinePic = OnlinePictureFragment(
                    "Pineapple",
                    Language("en")
                ).show(parentFragmentManager, null)
            }

        }
    }.root

}