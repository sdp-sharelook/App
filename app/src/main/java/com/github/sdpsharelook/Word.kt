package com.github.sdpsharelook

import android.graphics.Bitmap
import android.location.Location
import com.github.sdpsharelook.language.Language
import java.util.*

data class Word(
    val source: String,
    val sourceLanguage: Language,
    val target: String,
    val targetLanguage: Language,
    val location: Location?,
    val savedDate: Date?,
    val picture: Bitmap?,
) {
    // fun synonyms(): Set<Word> = TODO("not implemented yet")
    // ...
}

