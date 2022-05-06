package com.github.sdpsharelook.di

import com.github.sdpsharelook.authorization.AuthProvider
import com.github.sdpsharelook.authorization.FireAuth
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindsModule {
    @Singleton
    @Binds
    abstract fun bindFireAuth(r: FireAuth): AuthProvider
}

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseAuth =
        FirebaseAuth.getInstance()
}