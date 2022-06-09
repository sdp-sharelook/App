package com.github.sdpsharelook

import com.github.sdpsharelook.di.StorageBindsModule
import com.github.sdpsharelook.section.Section
import com.github.sdpsharelook.storage.IRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [StorageBindsModule::class]
)
class FakeStorageModuleUnitTests {
    @Provides
    @Singleton
    fun anyRepo(): IRepository<Any> = object : IRepository<Any> {
        override fun flow(name: String): Flow<Result<Any?>> =
            flowOf(Result.success("Hello World!"))

        override suspend fun insert(name: String, entity: Any) = Unit
        override suspend fun read(name: String): Any? = null
        override suspend fun update(name: String, entity: Any) = Unit
        override suspend fun delete(name: String, entity: Any) = Unit
    }

    @Provides
    @Singleton
    fun stringListRepo(): IRepository<List<String>> = object : IRepository<List<String>> {
        override fun flow(name: String): Flow<Result<List<String>?>> =
            flowOf(Result.success(listOf("Hello World!")))

        override suspend fun insert(name: String, entity: List<String>) = Unit
        override suspend fun read(name: String): List<String>? = null
        override suspend fun update(name: String, entity: List<String>) = Unit
        override suspend fun delete(name: String, entity: List<String>) = Unit
    }

    @Provides
    @Singleton
    fun wordListRepo(): IRepository<List<Word>> = object : IRepository<List<Word>> {
        private var word: Word = Word(source= "Hola", target = "Bonjour")
        private var wordList: MutableList<Word> = mutableListOf(word)
        private var flow = Channel<Result<List<Word>?>>()

        override fun flow(name: String): Flow<Result<List<Word>?>> {
            return flow.receiveAsFlow().onStart { emit(Result.success(wordList)) }
        }

        override suspend fun insert(name: String, entity: List<Word>) = Unit
        override suspend fun read(name: String): List<Word>? = null
        override suspend fun update(name: String, entity: List<Word>) = Unit
        override suspend fun delete(name: String, entity: List<Word>) = entity.forEach {
            wordList.remove(it)
            flow.send(Result.success(wordList))
        }
    }

    @Provides
    @Singleton
    fun sectionRepo(): IRepository<List<Section>> = object : IRepository<List<Section>> {
        private var sectionList: MutableList<Section> = mutableListOf()
        private var flow = Channel<Result<List<Section>?>>()
        override fun flow(name: String): Flow<Result<List<Section>?>> =
            flow.receiveAsFlow()

        override suspend fun insert(name: String, entity: List<Section>) = entity.forEach {
            if (sectionList.contains(it)){
                val i = sectionList.indexOf(it)
                sectionList.removeAt(i)
            }
            sectionList.add(it)
            flow.send(Result.success(sectionList))
        }

        override suspend fun read(name: String): List<Section>? = null
        override suspend fun update(name: String, entity: List<Section>) = Unit
        override suspend fun delete(name: String, entity: List<Section>) = entity.forEach {
            sectionList.remove(it)
            flow.send(Result.success(sectionList))
        }
    }
}