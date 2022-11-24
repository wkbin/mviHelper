package me.wkbin.movie.app.ui.vm

import androidx.lifecycle.MutableLiveData
import me.wkbin.movie.app.ui.mvi.VideoDetailEvent
import me.wkbin.movie.app.ui.mvi.VideoDetailState
import me.wkbin.mvihelper.base.BaseViewModel
import me.wkbin.mvihelper.core.DefaultEffect

class VideoDetailVM:BaseViewModel<VideoDetailState,DefaultEffect,VideoDetailEvent>() {
    override val _viewStates: MutableLiveData<VideoDetailState>
        get() = MutableLiveData(VideoDetailState())


}