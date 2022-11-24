package me.wkbin.movie.app.util.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

class GSYVideoLifeCycle(
    private val videoPlayer: StandardGSYVideoPlayer,
    private val orientationUtils: OrientationUtils
) : LifecycleEventObserver {

    private var isPause = false

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                videoPlayer.currentPlayer.onVideoPause()
                isPause = true
            }
            Lifecycle.Event.ON_RESUME -> {
                videoPlayer.currentPlayer.onVideoResume()
                isPause = false
            }
            Lifecycle.Event.ON_DESTROY -> {
                GSYVideoManager.releaseAllVideos()
                orientationUtils.releaseListener()
            }
            else -> {}
        }
    }
}