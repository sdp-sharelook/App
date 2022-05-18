package com.github.sdpsharelook

import android.os.Build
import androidx.annotation.RequiresApi
import com.github.sdpsharelook.language.Language
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.Expose
import java.time.Instant
import java.util.*

@IgnoreExtraProperties
data class Word(
    val uid: String = "",
    val source: String? = "",
    val sourceLanguage: Language? = Language.auto,
    val target: String? = "",
    val targetLanguage: Language? = Language.auto,
    @Expose(serialize = true, deserialize = true)
    var location: LatLng? = null,
    val savedDate: Date? = null,
    val picture: String? = null,
    val isFavourite: Boolean? = false,
) {
    constructor(uid: String) : this(uid, "", null, null, null, null, null, "", false)

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

    @RequiresApi(Build.VERSION_CODES.O)
    companion object {
        val testWord by lazy {
            Word(
                "testinguidneveractuallyusethis",
                "test",
                Language("French"),
                "test",
                Language("English"),
                LatLng(46.0, 6.0),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Date.from(
                    Instant.ofEpochMilli(1000000000)
                ) else null,
                "gs://billinguee.appspot.com/Pepe_rare-2469629177",
                true
            )
        }
    }
}

data class DBWord(
    val source: String = "",
    val sourceLanguage: String = "",
    val target: String = "",
    val targetLanguage: String = "",
    val location: String? = "",
    val savedDate: String? = "",
    val picture: String? = "",
    val uid: String = "",
    val isFavourite: Boolean = false,
) {
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

    constructor() : this("", "", "", "", "", "", "", "", false)
}
