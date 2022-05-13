package com.github.sdpsharelook.onlinePictures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.github.sdpsharelook.databinding.FragmentOnlinePictureSelectionBinding
import com.github.sdpsharelook.language.Language
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @param activity: the parent activity of fragment
 * @param keyword: the keyword to search
 */
class OnlinePictureFragment(
    private val keyword: String,
    private val language: Language,
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        FragmentOnlinePictureSelectionBinding.inflate(inflater, container, false)
            .apply {
                CoroutineScope(Dispatchers.IO).launch {
                    val pictures = GoogleImageApi.search(keyword, language) ?: listOf()
                    val fetchedPictures = ArrayList<OnlinePicture>()
                    if (pictures.isEmpty())
                        CoroutineScope(Dispatchers.Main).launch {
                            linearLayoutPictures.visibility = View.GONE
                            progressBarApiRequest.visibility = View.GONE
                            textViewNoResult.visibility = View.VISIBLE
                        }
                    else pictures.forEach {
                        // this line just fetch
                        it.thumbnail
                        fetchedPictures.add(it)
                        val adapter = OnlinePictureAdapter(requireContext(), fetchedPictures)
                        CoroutineScope(Dispatchers.Main).launch {
                            linearLayoutPictures.visibility = View.VISIBLE
                            progressBarThumbnailsFetching.apply {
                                progressBarThumbnailsFetching.progress =
                                    (100 * fetchedPictures.size) / pictures.size
                                if (fetchedPictures.size == pictures.size) visibility =
                                    View.GONE
                                else
                                    visibility = View.VISIBLE
                                progressBarApiRequest.visibility = View.GONE
                            }
                            gridViewPictures.adapter = adapter
                            gridViewPictures.setOnItemClickListener { _, _, i, _ ->
                                dismiss()
                                val picture = adapter.getItem(i) as OnlinePicture
                                // TODO use picture here
                            }
                        }
                    }
                }
            }.root
}
