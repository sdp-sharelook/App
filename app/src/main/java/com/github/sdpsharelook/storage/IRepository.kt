package com.github.sdpsharelook.storage

import com.github.sdpsharelook.Word
import com.github.sdpsharelook.section.Section
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Generic repository contract
 */
interface IRepository<T> {
    /**
     * Gets an asynchronous data stream of [T]
     *
     * @param name identifier of entity
     * @return [Flow] of changes in the database at [name]
     */
    fun flow(name: String = "test"): Flow<Result<@JvmSuppressWildcards T?>>

    /**
     * Insert repository entity into existing list
     *
     * @param name identifier of entity
     * @param entity Entity
     */
    suspend fun insert(name: String = "test", entity: T)

    /**
     * Read data at [name] once asynchronously.
     *
     * @param name identifier of entity
     * @return [T] or null
     */
    suspend fun read(name: String = "test"): T?

    /**
     * Update data entry at [name].
     *
     * Note: will not create entry, for that use [insert]
     *
     * @param name Caution: wrong [name] can overwrite data.
     * @param entity Entity
     */
    suspend fun update(name: String = "test", entity: T)

    /**
     * Delete repository entry at [name].
     *
     * @param name identifier of entity
     */
    suspend fun delete(name : String = "test")

    /**
     * Delete repository entry at [name].
     *
     * @param name identifier of entity
     * @param entity: Word
     */
    suspend fun deleteWord(name : String = "test", entity: Word)

    /**
     * Completely fuck up the dependency injection because
     * it's too damn hard to make a new implementation of [IRepository]
     *
     * And frankly that's not my job to fix
     *
     * @return an abstraction leak, my man
     */
    fun flowSection(): Flow<Result<List<Section>?>> = emptyFlow()

    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity List of words
     */
    suspend fun insertList(name: String, entity: T) = Unit

    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity Section
     */
    suspend fun insertSection(entity: Section) = Unit

    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity Section
     */
    suspend fun deleteSection(entity: Section) = Unit

}