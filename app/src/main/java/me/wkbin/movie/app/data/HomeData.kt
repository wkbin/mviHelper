package me.wkbin.movie.app.data

import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class HomeData(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "pic")
    val pic: String,
    @Json(name = "remarks")
    val remarks: String
)