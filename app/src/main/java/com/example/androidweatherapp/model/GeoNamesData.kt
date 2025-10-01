package com.example.androidweatherapp.model

data class GeoNamesResponse(
    val geonames: List<Geoname>,
    val totalResultsCount: Int
) {
    data class Geoname(
        val adminName1: String,
        val countryName: String,
        val lat: String,
        val lng: String,
        val name: String,
        val population: Int
    )
}