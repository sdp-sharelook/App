package com.github.sdpsharelook.textToSpeech

import android.content.Context
import android.widget.*
import com.github.sdpsharelook.language.Language
import android.speech.tts.TextToSpeech as GoogleTextToSpeech

class TextToSpeech(private val ctx: Context) {
    private var tts: GoogleTextToSpeech? = null

    init {
        tts = GoogleTextToSpeech(ctx) {
            when (it) {
                GoogleTextToSpeech.SUCCESS -> {
                    tts?.setPitch(pitch)
                    tts?.setSpeechRate(speechRate)
                    tts?.language = language.locale
                }
                else -> {
                    Toast.makeText(
                        ctx,
                        "An error happened while creating the TextToSpeech object",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private var _language = Language.default
    var language
        get() = _language
        set(value) {
            _language = value
            tts?.language = value.locale
        }
    private var _speechRate = .5f
    var speechRate
        get() = _speechRate
        set(value) {
            _speechRate = value
            tts?.setSpeechRate(value)
        }
    private var _pitch = .5f
    var pitch
        get() = _pitch
        set(value) {
            _pitch = value
            tts?.setPitch(value)
        }

    fun speak(message: String) =
        tts?.speak(message, GoogleTextToSpeech.QUEUE_FLUSH, null, null)
            ?: Toast.makeText(ctx, "The TextToSpeech object is null", Toast.LENGTH_SHORT).show()

}