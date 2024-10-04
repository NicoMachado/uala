package com.example.uala.data

import android.util.Log
import com.example.uala.data.datasource.RemoteDataSource
import com.example.uala.domain.models.City
import com.example.uala.domain.repository.CitiesRepository


class CitiesRepositoryImpl(
    private val dataSource: RemoteDataSource
): CitiesRepository {
    private var cities = listOf<City>()

    override suspend fun getCities(): List<City> {
        cities.ifEmpty {
            val citiesResult = dataSource.getCities()
            citiesResult.onSuccess {
                cities = it.map { c -> c.toModel() }
            }
        }
        return cities.sortedWith(
            compareBy<City> { it.name }
                .thenBy { it.country }
        )
    }
    override fun getFavourites() = cities.filter { city -> city.isFavourite }

    override suspend fun toggleFavourite(city: City) {
        cities = cities.map {
            if (it.id == city.id) {
                it.copy(isFavourite = !it.isFavourite)
            } else {
                it
            }
        }
    }
}
