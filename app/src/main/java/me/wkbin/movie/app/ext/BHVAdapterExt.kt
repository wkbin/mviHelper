package me.wkbin.movie.app.ext

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter

fun BaseQuickAdapter<*, *>.loadMore(loadMore: LoadMoreDsl.() -> Unit): QuickAdapterHelper {
    val loadMoreDsl = LoadMoreDsl()
    loadMore.invoke(loadMoreDsl)
    return QuickAdapterHelper.Builder(this)
        .setTrailingLoadStateAdapter(object : TrailingLoadStateAdapter.OnTrailingListener {
            override fun onLoad() {
                loadMoreDsl.onLoadOrFail?.invoke()
                loadMoreDsl.onLoad?.invoke()
            }

            override fun onFailRetry() {
                loadMoreDsl.onLoadOrFail?.invoke()
                loadMoreDsl.onFailRetry?.invoke()
            }

            override fun isAllowLoading(): Boolean {
                return loadMoreDsl.isAllowLoading
            }
        }).build()
}

class LoadMoreDsl {
    var onLoad: (() -> Unit)? = null
    var onFailRetry: (() -> Unit)? = null
    var onLoadOrFail: (() -> Unit)? = null
    var isAllowLoading = true
}