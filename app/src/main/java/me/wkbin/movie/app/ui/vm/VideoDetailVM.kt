package me.wkbin.movie.app.ui.vm

import androidx.lifecycle.MutableLiveData
import me.wkbin.movie.app.api.Net
import me.wkbin.movie.app.ui.mvi.VideoDetailEvent
import me.wkbin.movie.app.ui.mvi.VideoDetailState
import me.wkbin.movie.app.ui.mvi.VideoLevelStatus
import me.wkbin.mvihelper.base.BaseViewModel
import me.wkbin.mvihelper.core.DefaultEffect
import me.wkbin.mvihelper.ext.rxRequest
import me.wkbin.mvihelper.ext.setState

class VideoDetailVM : BaseViewModel<VideoDetailState, DefaultEffect, VideoDetailEvent>() {
    override val _viewStates: MutableLiveData<VideoDetailState> =
        MutableLiveData(VideoDetailState())

    override fun process(viewEvent: VideoDetailEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is VideoDetailEvent.OnGetVideoDetail -> getVideoDetail(
                viewEvent.videoId,
                viewEvent.deviceId,
                viewEvent.userId
            )

            is VideoDetailEvent.OnSelectLevel -> {

            }
        }
    }

    /**
     * 获取电影详情
     */
    private fun getVideoDetail(videoId: String, deviceId: String, userId: String?) {
        rxRequest {
            onRequest = {
                val videoDetail = Net.getVideoDetailData(videoId, deviceId, userId).await()
                _viewStates.setState {
                    copy(detailData = videoDetail)
                }
                if (videoDetail.playMedias.isNotEmpty()) {
                    _viewStates.setState {
                        copy(
                            status = VideoLevelStatus(0, videoDetail.playMedias.first().playUrl)
                        )
                    }
                }

            }
            showloading = true
        }
    }

}