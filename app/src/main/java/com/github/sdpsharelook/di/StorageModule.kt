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
@InstallIn(ActivityComponent::class)
abstract class StorageBindsModule {
    @Binds
    abstract fun bindAnyRepo(r: RTDBAnyRepository): IRepository<Any>

    @Binds
    abstract fun bindWordListRepo(r: RTDBWordListRepository): IRepository<List<@JvmSuppressWildcards String>>
}

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase =
        FirebaseDatabase.getInstance("https://billinguee-default-rtdb.europe-west1.firebasedatabase.app/")

//    @Provides
//    @Singleton
//    fun bindAnyRepo(r: RTDBAnyRepository): IRepository<Any> = r
//
//    @Provides
//    @Singleton
//    fun bindWordListRepo(r: RTDBWordListRepository): IRepository<List<String>> = r
}