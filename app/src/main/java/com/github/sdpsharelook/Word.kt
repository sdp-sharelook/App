package com.github.sdpsharelook

import android.os.Build
import androidx.annotation.RequiresApi
import com.github.sdpsharelook.language.Language
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule

@IgnoreExtraProperties
@Serializable
data class Word(
    val uid: String = "",
    val source: String? = "",
    val sourceLanguage: Language? = Language.auto,
    val target: String? = "",
    val targetLanguage: Language? = Language.auto,
    var location: Position? = null,
    val savedDate: Instant? = null,
    val picture: String? = null,
    val isFavourite: Boolean? = false,
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

    @RequiresApi(Build.VERSION_CODES.O)
    companion object {
        val serializersModule: SerializersModule = SerializersModule {
        }
        val testWord by lazy {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Word(
                    "testinguidneveractuallyusethis",
                    "test",
                    Language("French"),
                    "test",
                    Language("English"),
                    Position(46.0, 6.0),
                    Instant.fromEpochMilliseconds(100000000000),
                    "gs://billinguee.appspot.com/Pepe_rare-2469629177",
                    true
                )
            } else {
                Word(
                    "testinguidneveractuallyusethis",
                    "test",
                    Language("French"),
                    "test",
                    Language("English"),
                    Position(46.0, 6.0),
                    null,
                    "gs://billinguee.appspot.com/Pepe_rare-2469629177",
                    true

                )

            }
        }
    }


}

