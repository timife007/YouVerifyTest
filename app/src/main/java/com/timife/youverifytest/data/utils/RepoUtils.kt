package com.timife.youverifytest.data.utils

import com.timife.youverifytest.domain.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

// Helper function to wrap the API request and handle database caching
fun <T, R> fetchAndCache(
    apiCall: suspend () -> Response<T>,   // API callback to fetch data
    cacheCall: suspend () -> List<R>,     // Call to fetch data from local DB
    insertInDb: suspend (T) -> Unit,      // Insert the fetched data into the database
    mapToResult: (R) -> R                 // Mapping function to map DB entity to the result model
): Flow<Resource<List<R>>> {
    return flow {
        emit(Resource.Loading()) // Emit loading state

        try {
            val response = apiCall() // Perform api request
            if (response.isSuccessful) {
                response.body()?.let { result ->
                    insertInDb(result) // insert api result into the database
                }
            }
            // Fetch data from the database
            val data = cacheCall().map { mapToResult(it) }

            if (data.isEmpty()) {
                emit(Resource.Error("No data found")) // emit error if no data
            } else {
                emit(Resource.Success(data)) // emit success with the result
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message)) // emit error on exception
        }
    }.flowOn(Dispatchers.IO) // execute on IO thread
}

// Helper function for database-only queries
fun <R> loadFromDb(
    dbCall: suspend () -> List<R>,    // Call to fetch data from DB
    mapToResult: (R) -> R             // Mapping function for DB entity
): Flow<Resource<List<R>>> {
    return flow {
        emit(Resource.Loading())

        try {
            val data = dbCall().map { mapToResult(it) }

            if (data.isEmpty()) {
                emit(Resource.Error("No data found"))
            } else {
                emit(Resource.Success(data))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }.flowOn(Dispatchers.IO)
}
