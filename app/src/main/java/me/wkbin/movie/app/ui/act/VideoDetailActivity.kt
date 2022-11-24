package me.wkbin.movie.app.ui.act


import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import com.gyf.immersionbar.ImmersionBar
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import me.wkbin.movie.app.ui.mvi.VideoDetailEvent
import me.wkbin.movie.app.ui.mvi.VideoDetailState
import me.wkbin.movie.app.ui.vm.VideoDetailVM
import me.wkbin.movie.app.util.IntentKey
import me.wkbin.movie.databinding.ActivityVideoDetailBinding
import me.wkbin.mvihelper.base.BaseVBActivity
import me.wkbin.mvihelper.core.DefaultEffect
import me.wkbin.mvihelper.ext.lazyIntentExtra
import me.wkbin.mvihelper.ext.observeState

class VideoDetailActivity :
    BaseVBActivity<VideoDetailState, DefaultEffect, VideoDetailEvent, VideoDetailVM, ActivityVideoDetailBinding>() {

    override val mViewModel: VideoDetailVM by viewModels()

    private val videoId by lazyIntentExtra<String>(IntentKey.VIDEO_ID)

    private var isPlay = false
    private var isPause = false
    private lateinit var orientationUtils: OrientationUtils

    override fun initView(savedInstanceState: Bundle?) {
        initPlay()
        mViewModel.process(VideoDetailEvent.OnGetVideoDetail(videoId!!, "123456aa"))
    }

    override fun setImmersionBarBlock(immersionBar: ImmersionBar) {
        immersionBar.fitsSystemWindows(false)
    }

    override fun onPause() {
        mViewBind.videoPlay.currentPlayer.onVideoPause()
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        mViewBind.videoPlay.currentPlayer.onVideoResume(false)
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            mViewBind.videoPlay.currentPlayer.release()
            orientationUtils.releaseListener()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (isPlay && !isPause) {
            mViewBind.videoPlay.onConfigurationChanged(this, newConfig, orientationUtils)
        }
    }


    private fun initPlay() {
        orientationUtils = OrientationUtils(this, mViewBind.videoPlay)
        // 初始化不贷款外部旋转
        orientationUtils.isEnable = false
        mViewBind.videoPlay.apply {
            titleTextView.isVisible = false
            backButton.isVisible = false
            fullscreenButton.setOnClickListener {
                orientationUtils.resolveByClick()
                startWindowFullscreen(this@VideoDetailActivity, true, true)
            }
        }
    }

    override fun onBackPressed() {
        orientationUtils.backToProtVideo()
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun renderViewState(viewStates: LiveData<VideoDetailState>) {
        val activity = this
        viewStates.observeState(activity, VideoDetailState::detailData) {
            it?.let { videoDetailData ->
                // 电影名称
                Log.d("renderViewState", "name == ${videoDetailData.name}")
                // 演员
                videoDetailData.actors
            }
        }
        viewStates.observeState(activity, VideoDetailState::status) {
            val position = it?.position ?: 0
            val playUrl = it?.playUrl ?: ""

            GSYVideoOptionBuilder().apply {
                setIsTouchWiget(true)
                setRotateViewAuto(false)
                setLockLand(false)
                setAutoFullWithSize(false)
                setShowFullAnimation(false)
                setNeedLockFull(true)
                setUrl(playUrl)
                setCacheWithPlay(true)
                setVideoAllCallBack(object : GSYSampleCallBack() {
                    override fun onPrepared(url: String?, vararg objects: Any?) {
                        super.onPrepared(url, *objects)
                        orientationUtils.isEnable = mViewBind.videoPlay.isRotateWithSystem
                        isPlay = true
                    }

                    override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                        super.onQuitFullscreen(url, *objects)
                        orientationUtils.backToProtVideo()
                    }
                })
                setLockClickListener { _, lock ->
                    orientationUtils.isEnable = !lock
                }
            }.build(mViewBind.videoPlay)
            mViewBind.videoPlay.startPlayLogic()
        }
    }


}