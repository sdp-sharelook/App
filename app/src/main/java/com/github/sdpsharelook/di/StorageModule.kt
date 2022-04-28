package com.github.sdpsharelook.di

import com.github.sdpsharelook.storage.IRepository
import com.github.sdpsharelook.storage.RTDBAnyRepository
import com.github.sdpsharelook.storage.RTDBWordListRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
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
    abstract fun bindWordListRepo(r: RTDBWordListRepository): IRepository<List<String>>
}

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase =
        FirebaseDatabase.getInstance("https://billinguee-default-rtdb.europe-west1.firebasedatabase.app/")
}