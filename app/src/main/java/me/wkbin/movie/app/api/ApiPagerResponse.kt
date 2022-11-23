package me.wkbin.movie.app.api
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class ApiPagerResponse<T>(
    @Json(name = "list")
    val list: List<T>,
    @Json(name = "pageNum")
    val pageNum: Int,
    @Json(name = "pageSize")
    val pageSize: Int,
    @Json(name = "total")
    val total: Int,
    @Json(name = "totalPage")
    val totalPage: Int
)