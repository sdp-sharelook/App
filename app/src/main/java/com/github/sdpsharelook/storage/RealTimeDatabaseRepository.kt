package com.github.sdpsharelook.storage

import kotlinx.coroutines.flow.Flow

/**
 * Real time database using repository design pattern.
 */
interface RealTimeDatabaseRepository {
    fun fetchValues(): Flow<Result<Any?>>
}