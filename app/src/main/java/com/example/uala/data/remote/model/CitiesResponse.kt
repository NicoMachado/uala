package com.example.uala.data.remote.model

import com.example.uala.domain.models.City
import com.google.gson.annotations.SerializedName

data class CitiesResponse(
    @SerializedName("_id") val id: Int,
    val coord: Coord,
    val country: String,
    val name: String
) {
    fun toModel() =
        City(
            id = id,
            name = name,
            country = country,
            coordinate = City.Coord(coord.lat, coord.lon),
        )

}