package me.wkbin.movie.app.ui.vm


import android.graphics.Bitmap
import androidx.constraintlayout.motion.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import me.wkbin.movie.app.api.Net
import me.wkbin.movie.app.ui.mvi.DefaultEffect
import me.wkbin.movie.app.ui.mvi.MainViewEvent
import me.wkbin.movie.app.ui.mvi.MainViewState
import me.wkbin.mvihelper.base.BaseViewModel
import me.wkbin.mvihelper.core.UiEffect
import me.wkbin.mvihelper.ext.rxRequest
import rxhttp.toAwait
import rxhttp.wrapper.param.RxHttp
import javax.inject.Inject


class MainVM : BaseViewModel<MainViewState, DefaultEffect, MainViewEvent>() {

    init {
        viewState = MainViewState()
    }


    override fun process(viewEvent: MainViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is MainViewEvent.OnSwipeRefresh -> getRecommendData(true)
            is MainViewEvent.OnLoadMore -> getRecommendData(false)
        }
    }

    private var pageIndex = 1

    private fun getRecommendData(isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 1
        } else {
            pageIndex++
        }
        rxRequest {
            onRequest = {
                val recommendList = Net.getHomeMovesData(pageIndex).await()
                viewState = viewState.copy(recommendData = recommendList)
            }
            loadingMessage = "请求推荐数据..."
            showloading = true
        }
    }

    private fun onSwipeRefresh() {
        uiEffect = UiEffect.ShowToast("刷新成功")
    }

    private fun onLoadMore() {
        uiEffect = UiEffect.ShowToast("加载成功")
    }


    fun loadData() {
        rxRequest {
            onRequest = {
                val bm =
                    RxHttp.get("https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA14d9AM.img?w=1920&h=1080&q=60&m=2&f=jpg")
                        .toAwait<Bitmap>().await()
                viewState = viewState.copy(bitmap = bm)
            }
            loadingMessage = "模拟请求网络..."
            showloading = true
        }
    }


    fun shoToast() {
        uiEffect = UiEffect.ShowToast("弹出一条吐司")
    }
}