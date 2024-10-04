package com.example.uala.domain.models


data class City(
    val id: Int,
    val name: String,
    val country: String,
    val coordinate: Coord,
    val isFavourite: Boolean = false
) {
    data class Coord(
        val lat: Double,
        val lon: Double
    )
}

