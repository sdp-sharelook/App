package com.github.sdpsharelook.textDetection

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextDetection {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun process(image: InputImage): String{
        var textResult = "Aucun text"
        if(recognizer.process(image)
            .addOnSuccessListener(
                OnSuccessListener<Text?> { texts ->
                    textResult = texts.text
                })
            .addOnFailureListener(
                OnFailureListener { e -> // Task failed with an exception
                    e.printStackTrace()
                }).isComplete){
            return textResult
        }
        return textResult
    }

    private fun processTextBlock(result: Text): String {
        return if(result.text.isBlank()){
            "Aucun Text"
        }else{
            result.text
        }
    }
}