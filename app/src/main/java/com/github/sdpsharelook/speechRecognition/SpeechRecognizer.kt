package com.github.sdpsharelook.speechRecognition

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener as GoogleRecognitionListener
import android.speech.RecognizerIntent
import android.widget.Toast
import android.speech.SpeechRecognizer as GoogleSpeechRecognizer
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.sdpsharelook.language.Language
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class SpeechRecognizer(val activity: AppCompatActivity) {
    private var hasPermissions = false
    private val speechRecognizer by lazy { GoogleSpeechRecognizer.createSpeechRecognizer(activity) }



        /** Function triggered when audio permission is not allowed
         */
    fun errorPermission() =
        Toast.makeText(
            activity,
            "Please give us the audio permission to use this feature",
            Toast.LENGTH_SHORT
        ).show()

    /** start the permission asking procedure if needed
     */
    fun checkPermissions() =
        if (
            ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            hasPermissions = true
        } else {
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission())
            {
                when (it) {
                    true -> hasPermissions = true
                    false -> errorPermission()
                }
            }.launch(
                android.Manifest.permission.RECORD_AUDIO
            )
        }

    /**
     * Create the intent to start the speech recognizer
     * @param language: the language to put in the intent (free form by default)
     */
    private fun createRecognizerIntent(language: String? = null): Intent =
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            language?.let {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, it)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, it)
            } ?: run {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            }
        }


    /** Create the recognition listener from the library
     */
    private fun createGoogleRecognitionListener(listener: RecognitionListener): GoogleRecognitionListener =
        object : android.speech.RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) = listener.onReady()

            override fun onBeginningOfSpeech() = listener.onBegin()

            override fun onRmsChanged(p0: Float) {}

            override fun onBufferReceived(results: ByteArray?) {}

            override fun onEndOfSpeech() = listener.onEnd()

            override fun onError(code: Int) = listener.onError()

            fun catchResults(results: Bundle?) =
                results?.getStringArrayList(GoogleSpeechRecognizer.RESULTS_RECOGNITION)?.let {
                    listener.onResults(it.joinToString(""))
                } ?: listener.onError()

            override fun onResults(results: Bundle?) = catchResults(results)

            override fun onPartialResults(results: Bundle?) = catchResults(results)

            override fun onEvent(p0: Int, p1: Bundle?) {}
        }

    /** Start the speech recognition
     * @param listener: the listener for callback when the result is ready
     * @param language: to eventually specify the language (free form by default)
     */
    fun recognizeSpeech(listener: RecognitionListener, language: String? = null) {
        val intent = createRecognizerIntent(language)
        val googleListener = createGoogleRecognitionListener(listener)
        checkPermissions()
        if (hasPermissions) {
            speechRecognizer.setRecognitionListener(googleListener)
            speechRecognizer.startListening(intent)
        }
    }

    /** Cancel the speech recognition (must call it before starting a new one)
     */
    fun cancel() = speechRecognizer.cancel()

    private var _availableLanguages: Set<Language>? = null
    suspend fun availableLanguages(): Set<Language> =
        _availableLanguages ?: suspendCoroutine { cont ->
            val br = object : BroadcastReceiver() {
                override fun onReceive(p0: Context?, p1: Intent?) {
                    val results = getResultExtras(true)
                    if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
                        val availableLanguages =
                            results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)
                                ?.map { Language.forLanguageTag(it) }?.toSet() ?: setOf()
                        _availableLanguages = availableLanguages
                        cont.resume(availableLanguages)
                    } else cont.resume(setOf())
                }
            }
            val intent = Intent(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)
            val filter = IntentFilter(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)
            activity.apply { registerReceiver(br, filter); startActivity(intent) }
        }

    private var _language: Language? = null
    var language
        get() = _language
        set(language) {
            val sr = this
            CoroutineScope(EmptyCoroutineContext).launch {
                if (language?.isAvailableForSR(sr) == true)
                    _language = language
                else _language = null
            }
        }
}
