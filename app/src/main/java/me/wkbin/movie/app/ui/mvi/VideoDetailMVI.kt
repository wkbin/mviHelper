package me.wkbin.movie.app.ui.mvi

import VideoDetailData

data class VideoDetailState(
    val detailData:VideoDetailData? = null
)

sealed class VideoDetailEvent{
    /**
     * 选集
     */
    data class OnSelectLevel(val position:Int, val playUrl:String):VideoDetailEvent()
}