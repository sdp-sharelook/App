package com.github.sdpsharelook.storage

import android.graphics.Bitmap
import android.media.Image
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject
import javax.security.auth.callback.Callback
interface ImageUrlCallback {
    //String may be null in case of failure
    fun onCallback(url: String?)
}

interface ImageStorer{
    suspend fun saveImage(image: Bitmap, urlCallback: ImageUrlCallback)
}
class ImageStorage @Inject constructor(
    private val storage : FirebaseStorage
): ImageStorer {
    override suspend fun saveImage(image: Bitmap, urlCallback: ImageUrlCallback) {
        var url: String? = "test"
        Dispatchers.IO.run{
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val data = baos.toByteArray()
            val id = UUID.randomUUID().toString()
            val uploadTask = storage.reference.child(id).putBytes(data)
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                storage.reference.child(id).downloadUrl
            }.addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    urlCallback.onCallback(task.result.toString())
                }else{
                    urlCallback.onCallback(null)
                }
            }.await()
        }
    }

}
