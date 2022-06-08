package com.github.sdpsharelook.di

import com.github.sdpsharelook.downloads.MLKitTranslatorDownloader
import com.github.sdpsharelook.downloads.TranslatorDownloader
import com.github.sdpsharelook.translate.MLKitTranslator
import com.github.sdpsharelook.translate.TranslationProvider
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TranslationModule {
    @Singleton
    @Binds
    abstract fun bindDownloader(r: MLKitTranslatorDownloader): TranslatorDownloader

    @Singleton
    @Binds
    abstract fun bindTranslator(r: MLKitTranslator): TranslationProvider
}

@Module
@InstallIn(SingletonComponent::class)
object MLKitModule {
    @Singleton
    @Provides
    fun provideLanguageIdentifier(): LanguageIdentifier = LanguageIdentification.getClient()

    @Singleton
    @Provides
    fun provideModelManager(): RemoteModelManager = RemoteModelManager.getInstance()
}