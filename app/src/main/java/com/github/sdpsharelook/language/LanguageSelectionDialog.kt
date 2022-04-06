package com.github.sdpsharelook.language

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.github.sdpsharelook.R
import com.github.sdpsharelook.translate.Translator
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LanguageSelectionDialog private constructor(
    private val activity: AppCompatActivity,
    private val languages: Set<Language>
) : Dialog(activity) {

    private fun search(text: String): LanguageAdapter =
        LanguageAdapter(
            activity,
            languages.filter { it.displayName.lowercase().contains(text.lowercase()) }.toSet()
        )

    private fun buildDialog(continuation: Continuation<Language?>) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_language_selection)
        val dialog = this
        setOnCancelListener { continuation.resume(null) }
        val listView = findViewById<ListView>(R.id.list_view_languages).apply {
            adapter = search("")
            setOnItemClickListener { _, _, i, _ ->
                dialog.dismiss()
                val language = adapter.getItem(i) as Language
                continuation.resume(language)
            }
        }
        findViewById<EditText>(R.id.edit_text_search_language).let {
            it.addTextChangedListener { _ ->
                listView.adapter = search(it.text.toString())
            }
        }
    }

    private suspend fun selectLanguage(): Language? = suspendCoroutine { cont ->
        buildDialog(cont)
        show()
    }

    companion object {
        suspend fun selectLanguage(
            activity: AppCompatActivity,
            languages: Set<Language> = Translator.availableLanguages
        ): Language? = LanguageSelectionDialog(activity, languages).selectLanguage()
    }
}