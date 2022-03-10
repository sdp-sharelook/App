package com.github.sdpsharelook

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import java.util.*


const val EXTRA_MESSAGE = "com.github.sdpsharelook.NAME"

class MainActivity : AppCompatActivity() {
    private var tts: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tts = TextToSpeech(this) {
            when (it) {
                TextToSpeech.SUCCESS -> {
                    tts?.setPitch(0.2f)
                    tts?.setSpeechRate(1.4f)
                    tts?.setLanguage(Locale.UK)
                    val spinner: Spinner? = findViewById(R.id.spinner_language)
                    val languages = tts?.availableLanguages
                    val adapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.activity_main,
                        languages?.map { it.displayCountry } ?: listOf("No available language"))
                    // spinner?.adapter = adapter
                    val seekbar_pitch_listener = object : OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar?, i: Int, b: Boolean) {
                            tts?.setSpeechRate(i.toFloat())
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    }
                    findViewById<SeekBar>(R.id.seekBar_pitch).setOnSeekBarChangeListener(
                        seekbar_pitch_listener
                    )
                }
                else -> {
                    toast("An error happened while creating the TextToSpeech object")
                }
            }
        }
    }

    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun speak(view: View) {
        val editText = findViewById<EditText>(R.id.mainName)
        val name = editText.text.toString()
        tts?.speak(name, TextToSpeech.QUEUE_FLUSH, null, null)
            ?: toast("The TextToSpeech object is null")
    }

    fun greet(view: View) {
        val editText = findViewById<EditText>(R.id.mainName)
        val name = editText.text.toString()
        val intent = Intent(this, GreetingActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, name)
        }
        startActivity(intent)
    }
}
