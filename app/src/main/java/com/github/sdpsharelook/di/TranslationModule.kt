package com.github.sdpsharelook.di

import com.github.sdpsharelook.authorization.AuthProvider
import com.github.sdpsharelook.authorization.FireAuth
import com.github.sdpsharelook.downloads.MLKitTranslatorDownloader
import com.github.sdpsharelook.downloads.TranslatorDownloader
import com.github.sdpsharelook.translate.MLKitTranslator
import com.github.sdpsharelook.translate.TranslationProvider
import com.google.firebase.auth.FirebaseAuth
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
    fun provideMlKitTranslatorDownloader(): MLKitTranslatorDownloader = MLKitTranslatorDownloader

    @Singleton
    @Provides
    fun provideMlKitTranslator(): MLKitTranslator = MLKitTranslator
}