package com.example.uala.domain.repository

import com.example.uala.domain.models.City

interface CitiesRepository {
    suspend fun getCities(): List<City>
    fun getFavourites(): List<City>
    suspend fun toggleFavourite(city: City)
}