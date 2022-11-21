package me.wkbin.movie.app.ui.mvi

import android.graphics.Bitmap
import me.wkbin.movie.app.data.HomeData


data class HomeViewState(
    val recommendData: List<HomeData>? = null,
    val bitmap:Bitmap? = null
)

sealed class HomeViewEvent {
    /**
     * 下拉刷新
     */
    object OnSwipeRefresh : HomeViewEvent()

    /**
     * 上拉加载
     */
    object OnLoadMore : HomeViewEvent()
}