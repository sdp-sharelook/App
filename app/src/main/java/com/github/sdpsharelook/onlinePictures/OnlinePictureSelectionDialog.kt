package com.github.sdpsharelook.onlinePictures

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.github.sdpsharelook.databinding.DialogOnlinePictureSelectionBinding
import com.github.sdpsharelook.language.Language
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @param activity: the parent activity of fragment
 * @param keyword: the keyword to search
 */
class OnlinePictureSelectionDialog(
    private val activity: FragmentActivity,
    private val keyword: String,
    private val language: Language,
) :
    Dialog(activity) {

    private fun buildDialog(continuation: Continuation<OnlinePicture?>) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setOnCancelListener { continuation.resume(null) }
        setContentView(
            DialogOnlinePictureSelectionBinding.inflate(LayoutInflater.from(activity))
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
                            val adapter = OnlinePictureAdapter(activity, fetchedPictures)
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
                                    continuation.resume(picture)
                                }
                            }
                        }
                    }
                }.root
        )
    }

    /**
     * launch the picture selection dialog.
     * @return : null if canceled or a OnlinePicture if the user has chosen one
     */
    suspend fun selectPicture(): OnlinePicture? = suspendCoroutine { cont ->
        buildDialog(cont)
        show()
    }

    companion object {
        /**
         *
         */
        suspend fun selectPicture(activity: FragmentActivity, keyword: String, language: Language) =
            OnlinePictureSelectionDialog(activity, keyword, language).selectPicture()
    }


}