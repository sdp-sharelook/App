package com.github.sdpsharelook.translate

import com.github.sdpsharelook.language.Language

interface TranslationProvider {
    val availableLanguages: List<Language>
    suspend fun detectLanguage(text: String): String

    /** Translate the text from src language to dst language using coroutines
     * @param text : String | Text in src language to translate in dst language
     * @return translationResult : String
     */
    suspend fun translate(text: String, src: String, dst: String): String
}