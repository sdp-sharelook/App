package com.github.sdpsharelook

import android.graphics.Bitmap
import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import com.github.sdpsharelook.language.Language
import com.google.android.datatransport.runtime.dagger.Provides
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable
import java.util.*
import javax.inject.Inject

data class Word(
    val uid: String = "",
    val source: String = "",
    val sourceLanguage: Language = Language.auto,
    val target: String = "",
    val targetLanguage: Language = Language.auto,
    val location: Location? = null,
    val savedDate: Date? = null,
    val picture: String? = null,
    val isFavourite: Boolean = false,
) : Serializable {

    constructor(uid: String) : this(uid,
        "",
        Language.auto,
        "",
        Language.auto,
        null,
        null,
        "",
        false)

    // fun synonyms(): Set<Word> = TODO("not implemented yet")
    // ...
    fun toMap(): Map<String, Any?> {
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

    fun toList(): List<String> =
        listOf(uid, source, sourceLanguage.tag, target, targetLanguage.tag)
}

fun List<String>.toWord(): Word {
    val (uid, source, sourceLangTag, target, targetLangTag) = this
    return Word(
        uid = uid,
        source = source,
        target = target,
        sourceLanguage = Language(sourceLangTag),
        targetLanguage = Language(targetLangTag)
    )
}
