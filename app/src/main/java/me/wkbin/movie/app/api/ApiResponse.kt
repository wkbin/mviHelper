package me.wkbin.movie.app.api

data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    val data: T
)