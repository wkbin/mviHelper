package me.wkbin.movie.app.ui.mvi

import android.graphics.Bitmap
import me.wkbin.movie.app.data.HomeData


data class MainViewState(
    val recommendData: List<HomeData>? = null,
    val bitmap:Bitmap? = null
)

sealed class MainViewEvent {
    /**
     * 下拉刷新
     */
    object OnSwipeRefresh : MainViewEvent()

    /**
     * 上拉加载
     */
    object OnLoadMore : MainViewEvent()
}