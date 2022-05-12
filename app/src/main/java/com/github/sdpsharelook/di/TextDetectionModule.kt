package com.github.sdpsharelook.di

import android.content.Context
import com.google.mlkit.common.MlKit
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TextDetectionModule {
    @Singleton
    @Provides
    fun provideTextRecognizer(): TextRecognizer{
        return TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }
}
