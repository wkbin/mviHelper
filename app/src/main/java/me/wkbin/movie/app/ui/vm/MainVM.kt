package me.wkbin.movie.app.ui.vm


import android.graphics.Bitmap
import me.wkbin.movie.app.api.Net
import me.wkbin.movie.app.ui.mvi.FetchStatus
import me.wkbin.movie.app.ui.mvi.MainViewEffect
import me.wkbin.movie.app.ui.mvi.MainViewEvent
import me.wkbin.movie.app.ui.mvi.MainViewState
import me.wkbin.mvihelper.base.BaseViewModel
import me.wkbin.mvihelper.core.UiEffect
import me.wkbin.mvihelper.ext.rxRequest
import rxhttp.toAwait
import rxhttp.wrapper.param.RxHttp


class MainVM : BaseViewModel<MainViewState, MainViewEffect, MainViewEvent>() {

    init {
        viewState = MainViewState(fetchStatus = FetchStatus.NotFetched, null)
    }

    override fun process(viewEvent: MainViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is MainViewEvent.OnSwipeRefresh -> onSwipeRefresh()
            is MainViewEvent.OnLoadMore -> onLoadMore()
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
                val bm = RxHttp.get("https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA14d9AM.img?w=1920&h=1080&q=60&m=2&f=jpg")
                    .toAwait<Bitmap>().await()
                viewState =  viewState.copy(fetchStatus = FetchStatus.Fetched, bitmap = bm)
            }
            loadingMessage = "模拟请求网络..."
            showloading = true
        }
    }


    fun shoToast() {
        uiEffect = UiEffect.ShowToast("弹出一条吐司")
    }
}