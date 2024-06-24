package com.example.mad_project.classes

import androidx.compose.runtime.mutableStateListOf

data class Feature(
    val id: String,
    val geometry: Geometry?,
    val properties: Properties?,
    val type: String?
)

data class Geometry(
    val type: String?,
    val coordinates: List<Double>
)

data class Properties(
    val xid: String?,
    val name: String?,
    val dist: Double?,
    val rate: Int?,
    val wikidata: String?,
    val kinds: String?,
    val description: String?,
    var images: MutableList<String> = mutableStateListOf(),
)

data class FeatureCollection(
    val type: String?,
    val features: List<Feature>
)
