package com.github.sdpsharelook.di

import com.github.sdpsharelook.Word
import com.github.sdpsharelook.storage.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageBindsModule {
    @Singleton
    @Binds
    abstract fun bindAnyRepo(r: RTDBAnyRepository): IRepository<Any>

    @Singleton
    @Binds
    abstract fun bindWordListRepo(r: RTDBWordListRepository): IRepository<Word>

    @Singleton
    @Binds
    abstract fun bindStringListRepo(r: RTDBStringListRepository): IRepository<List<String>>

    @Singleton
    @Binds
    abstract  fun bindImageStorage(r: ImageStorage): ImageStorer
}

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase =
        FirebaseDatabase.getInstance("https://billinguee-default-rtdb.europe-west1.firebasedatabase.app/")
    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage =
        FirebaseStorage.getInstance()
}