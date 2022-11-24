package me.wkbin.movie.app.ui.mvi

import VideoDetailData


data class VideoDetailState(
    val detailData: VideoDetailData? = null,
    val status:VideoLevelStatus? = null
)

data class VideoLevelStatus(
    val position: Int,
    val playUrl:String,
)

sealed class VideoDetailEvent {
    /**
     * 选集
     */
    data class OnSelectLevel(val position: Int, val playUrl: String) : VideoDetailEvent()

    /**
     * 获取详情
     */
    data class OnGetVideoDetail(
        val videoId: String,
        val deviceId: String,
        val userId: String? = null
    ) : VideoDetailEvent()
}