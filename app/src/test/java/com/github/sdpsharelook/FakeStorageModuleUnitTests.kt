package com.github.sdpsharelook

import com.github.sdpsharelook.di.StorageBindsModule
import com.github.sdpsharelook.revision.UiEvent
import com.github.sdpsharelook.section.Section
import com.github.sdpsharelook.storage.IRepository
import com.github.sdpsharelook.storage.RTDBWordListRepository
import com.github.sdpsharelook.storage.RTDBWordListRepositoryTest
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import okhttp3.internal.notifyAll
import org.mockito.kotlin.mock
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

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
        override suspend fun delete(name: String) = Unit
        override suspend fun deleteWord(name: String, entity: Word) = Unit
    }

    @Provides
    @Singleton
    fun stringListRepo(): IRepository<List<String>> = object : IRepository<List<String>> {
        override fun flow(name: String): Flow<Result<List<String>?>> =
            flowOf(Result.success(listOf("Hello World!")))

        override suspend fun insert(name: String, entity: List<String>) = Unit
        override suspend fun read(name: String): List<String>? = null
        override suspend fun update(name: String, entity: List<String>) = Unit
        override suspend fun delete(name: String) = Unit
        override suspend fun deleteWord(name: String, entity: Word) = Unit
    }

    @Provides
    @Singleton
    fun wordListRepo(): IRepository<List<Word>> = object : IRepository<List<Word>> {
        private var section: Section = Section("test", 0, "test" )
        private var sectionList: MutableList<Section> = mutableListOf(section)
        private var flow = Channel<Result<List<Section>?>>()

        override fun flow(name: String): Flow<Result<List<Word>?>> =
            flowOf(Result.failure(CancellationException("test")))

        override fun flowSection(): Flow<Result<List<Section>?>> {
            return flow.receiveAsFlow()
        }

        override suspend fun insertSection(entity: Section) {
            sectionList.add(entity)
            flow.send(Result.success(sectionList))
        }

        override suspend fun insert(name: String, entity: List<Word>) = Unit
        override suspend fun read(name: String): List<Word>? = null
        override suspend fun update(name: String, entity: List<Word>) = Unit
        override suspend fun delete(name: String) = Unit
        override suspend fun deleteWord(name: String, entity: Word) = Unit
    }

}