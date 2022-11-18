package me.wkbin.mvihelper.ext


import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.wkbin.mvihelper.base.BaseViewModel
import me.wkbin.mvihelper.core.UiEffect

/**
 * 封装一下 RxHttp请求
 * @receiver BaseViewModel
 *
 */
fun BaseViewModel<*, *, *>.rxRequest(requestDslClass: HttpRequestDsl.() -> Unit) {
    val httpRequestDsl = HttpRequestDsl()
    requestDslClass(httpRequestDsl)
    viewModelScope.launch {
        // 线程请求之前
        if (httpRequestDsl.showloading) {
            uiEffect = UiEffect.ShowLoadDialog(httpRequestDsl.loadingMessage)
        }
        try {
            // 请求成功
            httpRequestDsl.onRequest.invoke(this)
        } catch (e: Throwable) {
            if(httpRequestDsl.showErrorToast){
                uiEffect = UiEffect.ShowToast("msg:${e.message} code:${e.code}")
            }
            httpRequestDsl.onError?.invoke(e)
        } finally {
            if (httpRequestDsl.showloading) {
                uiEffect = UiEffect.DismissLoadDialog
            }
            httpRequestDsl.onFinish
        }
    }
}


class HttpRequestDsl {
    /**
     * 请求工作 在这里执行网络接口请求，然后回调成功数据
     */
    var onRequest: suspend CoroutineScope.() -> Unit = {}

    /**
     * 错误回调，默认为null 如果你传递了他 那么就代表你请求失败的逻辑你自己处理
     */
    var onError: ((Throwable) -> Unit)? = null

    var onFinish: (() -> Unit)? = null

    /**
     * 请求时loading类型 默认请求时不显示loading
     */
    var showloading = false

    /**
     * 请求失败弹出toast提示
     */
    var showErrorToast = true

    /**
     * loading文案
     */
    var loadingMessage: String = "请求网络中..."
}

