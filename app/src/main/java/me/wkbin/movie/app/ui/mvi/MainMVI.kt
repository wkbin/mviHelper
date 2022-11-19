package me.wkbin.movie.app.ui.mvi

import android.graphics.Bitmap


data class MainViewState(
    val list:Any? = null,
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