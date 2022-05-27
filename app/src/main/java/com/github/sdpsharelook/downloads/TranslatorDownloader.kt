package com.github.sdpsharelook.downloads

import com.github.sdpsharelook.language.Language

interface TranslatorDownloader {
    suspend fun downloadedLanguages(): List<Language>?

    suspend fun deleteLanguage(language: Language): Boolean

    suspend fun downloadLanguage(language: Language, requireWifi: Boolean = false): Boolean
}