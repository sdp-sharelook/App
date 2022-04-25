package com.github.sdpsharelook.Section

import android.graphics.Bitmap
import java.io.Serializable

class SectionWord(var sourceText: String, var translatedText: String, var image: Bitmap?): Serializable {

    fun toList():List<String>{
        return listOf(sourceText, translatedText)
    }
}