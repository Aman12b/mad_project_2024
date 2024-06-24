package com.example.mad_project.data_classes

data class CountryData(val continents: List<Continent>)
data class Continent(val name: String, val countries: List<Country>)
data class Country(val name: String, val cities: List<City>)
data class City(val name: String, val iata_codes: List<String>, val coordinates: Coordinates)
data class Coordinates(val lat: Double, val lng: Double)