package me.wkbin.movie.app.ui.mvi

import me.wkbin.movie.app.data.HomeData


data class HomeViewState(
    val loadStatus: LoadStatus = LoadStatus.FirstLoad(true),
    val homeMovesData:List<HomeData>? = null,
    val homeTvData:List<HomeData>? = null,
    val homeCartoonData:List<HomeData>? = null,
    val recommendData: List<HomeData>? = null,
)

sealed class LoadStatus {
    /**
     * 第一次加载
     */
    data class FirstLoad(val hasNoMore:Boolean) : LoadStatus()

    /**
     * 加载更多
     */
    data class LoadMore(val hasNoMore:Boolean) : LoadStatus()
}

sealed class HomeViewEvent {
    /**
     * 下拉刷新
     */
    object OnSwipeRefresh : HomeViewEvent()

    /**
     * 上拉加载
     */
    data class OnLoadMore(
        val eventType: HomeViewEventType
    ) : HomeViewEvent()
}

sealed class HomeViewEventType {
    /**
     * 推荐
     */
    object Recommend : HomeViewEventType()

    /**
     * 电影
     */
    object Movie : HomeViewEventType()

    /**
     * 电视剧
     */
    object TVDrama : HomeViewEventType()

    /**
     * 动漫
     */
    object Cartoon : HomeViewEventType()
}