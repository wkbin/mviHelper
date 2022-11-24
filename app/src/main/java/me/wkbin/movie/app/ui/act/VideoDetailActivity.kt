package me.wkbin.movie.app.ui.act


import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import me.wkbin.movie.app.ui.mvi.VideoDetailEvent
import me.wkbin.movie.app.ui.mvi.VideoDetailState
import me.wkbin.movie.app.ui.vm.VideoDetailVM
import me.wkbin.movie.databinding.ActivityVideoDetailBinding
import me.wkbin.mvihelper.base.BaseVBActivity
import me.wkbin.mvihelper.core.DefaultEffect

class VideoDetailActivity :
    BaseVBActivity<VideoDetailState, DefaultEffect, VideoDetailEvent, VideoDetailVM, ActivityVideoDetailBinding>() {
    override val mViewModel: VideoDetailVM by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun renderViewState(viewStates: LiveData<VideoDetailState>) {

    }

}