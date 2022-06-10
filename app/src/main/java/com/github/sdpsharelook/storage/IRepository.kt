package com.github.sdpsharelook.storage

import com.github.sdpsharelook.Word
import com.github.sdpsharelook.section.Section
import kotlinx.coroutines.flow.*

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
    suspend fun read(name: String = "test"): T? =
        flow(name).first().getOrNull()

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
    suspend fun delete(name: String = "test", entity: T)

    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity List of words
     */
    suspend fun insertMultiple(name: String, entity: List<T>) =
        entity.forEach { insert(name, it) }

    companion object {
        const val SECTION_LIST = "SectionList"
    }
}