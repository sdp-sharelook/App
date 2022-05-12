package com.github.sdpsharelook

import android.graphics.Bitmap
import android.location.Location
import com.github.sdpsharelook.language.Language
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class Word(
    val uid: String="",
    val source: String? = "",
    val sourceLanguage: Language? =Language.auto ,
    val target: String? = "",
    val targetLanguage: Language? = Language.auto,
    val location: Location? = null,
    val savedDate: Date? = null,
    val picture: String? = null,
    val isFavourite: Boolean?= false,
): Serializable {
    constructor(uid: String) : this(uid,"",null,null,null,null,null,"",false)

    // fun synonyms(): Set<Word> = TODO("not implemented yet")
    // ...
    fun toMap(): Map<String,Any?>{
        return mapOf(
            "uid" to uid,
            "source" to source,
            "sourceLanguage" to sourceLanguage,
            "targetLanguage " to targetLanguage,
            "location" to location,
            "savedDate" to savedDate,
            "pictureUrl" to picture,
            "isFavorite" to isFavourite
        )
    }
}
data class dbWord(
    val source: String="",
    val sourceLanguage: String="",
    val target: String="",
    val targetLanguage: String="",
    val location: String?="",
    val savedDate: String?="",
    val picture: String?="",
    val uid: String="",
    val isFavourite: Boolean =false,
) {
    // fun synonyms(): Set<Word> = TODO("not implemented yet")
    // ...
    fun toMap(): Map<String,Any?>{
        return mapOf(
            "uid" to uid,
            "source" to source,
            "sourceLanguage" to sourceLanguage,
            "targetLanguage " to targetLanguage,
            "location" to location,
            "savedDate" to savedDate,
            "pictureUrl" to picture,
            "isFavorite" to isFavourite
        )
    }
    constructor(): this("","","","","","","","",false)
}