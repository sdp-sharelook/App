package com.github.sdpsharelook.storage

import kotlinx.coroutines.flow.Flow

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
}