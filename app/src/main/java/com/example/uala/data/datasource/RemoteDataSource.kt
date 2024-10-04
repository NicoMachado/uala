package com.example.uala.data.datasource

import com.example.uala.data.remote.ApiService
import com.example.uala.data.remote.model.CitiesResponse

class RemoteDataSource(
    private val api: ApiService
) {
    suspend fun getCities(): Result<List<CitiesResponse>> {
        return try {
            val response = api.getCities()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}



