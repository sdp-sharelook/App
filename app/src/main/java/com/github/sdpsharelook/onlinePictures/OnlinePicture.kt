package com.github.sdpsharelook.onlinePictures

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.github.sdpsharelook.R
import java.io.InputStream
import java.net.URL

/**
 *
 */
data class OnlinePicture(
    /**
     * the link to the thumbnail picture
     */
    val thumbnailLink: String,
    /**
     * the source (domain) of the website hosting the picture
     */
    val mediumLink: String,
    /**
     * the link of the website hosting the picture
     */
    val title: String,
) {
    /**
     * fetch the thumbnail bitmap from the link in 'thumbnail'
     */
    val thumbnail: Bitmap by lazy {
        try {
            BitmapFactory.decodeStream(URL(thumbnailLink).content as InputStream)
        } catch (e: Throwable) {
            BitmapFactory.decodeResource(null, R.drawable.ic_no_image)
        }
    }

    /**
     * fetch the original bitmap from the link in 'original'
     */
    val medium: Bitmap by lazy {
        BitmapFactory.decodeStream(URL(mediumLink).content as InputStream)
    }
}
