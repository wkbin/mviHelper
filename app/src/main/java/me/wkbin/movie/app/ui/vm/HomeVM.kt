package me.wkbin.movie.app.ui.vm


import androidx.lifecycle.MutableLiveData
import me.wkbin.movie.app.api.Net
import me.wkbin.mvihelper.core.DefaultEffect
import me.wkbin.movie.app.ui.mvi.HomeViewEvent
import me.wkbin.movie.app.ui.mvi.HomeViewEventType
import me.wkbin.movie.app.ui.mvi.HomeViewState
import me.wkbin.movie.app.ui.mvi.LoadStatus
import me.wkbin.mvihelper.base.BaseViewModel
import me.wkbin.mvihelper.ext.rxRequest
import me.wkbin.mvihelper.ext.setState


class HomeVM : BaseViewModel<HomeViewState, HomeViewEvent>() {

    private val _mockBanner = listOf(
        "http://pic.netbian.com/uploads/allimg/221121/234549-1669045549622d.jpg",
        "http://pic.netbian.com/uploads/allimg/221115/153253-1668497573e1d2.jpg",
    )
    val mockBanner get() = _mockBanner

    override fun process(viewEvent: HomeViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is HomeViewEvent.OnSwipeRefresh -> {
                getRecommendData(true)
                getHomeMoves(true)
                getHomeTv(true)
                getHomeCartoon(true)
            }
            is HomeViewEvent.OnLoadMore -> when (viewEvent.eventType) {
                HomeViewEventType.Recommend -> getRecommendData(false)
                HomeViewEventType.Cartoon ->  getHomeCartoon(false)
                HomeViewEventType.Movie -> getHomeMoves(false)
                HomeViewEventType.TVDrama -> getHomeTv(false)
            }
        }
    }

    private var recommendPageIndex = 1
    private var homeMovesPageIndex = 1
    private var homeTvPageIndex = 1
    private var homeCartoonIndex = 1

    /**
     * 获取首页推荐数据
     */
    private fun getRecommendData(isRefresh: Boolean = false) {
        if (isRefresh) {
            recommendPageIndex = 1
        }
        rxRequest {
            onRequest = {
                val recommendList = Net.getRecommendData(recommendPageIndex).await()
                val hasNoMore = recommendPageIndex >= recommendList.totalPage
                val status  = if (isRefresh) LoadStatus.FirstLoad(hasNoMore) else LoadStatus.LoadMore(hasNoMore)
                _viewStates.setState {
                    copy(
                        loadStatus = status,
                        recommendData = recommendList.list
                    )
                }
                recommendPageIndex++
            }
        }
    }

    /**
     * 获取首页电影数据
     */
    private fun getHomeMoves(isRefresh: Boolean = false){
        if (isRefresh) {
            homeMovesPageIndex = 1
        }
        rxRequest {
            onRequest = {
                val homeMovesList = Net.getHomeMovesData(homeMovesPageIndex).await()
                val hasNoMore = homeMovesPageIndex >= homeMovesList.totalPage
                _viewStates.setState {
                    copy(
                        loadStatus = if (isRefresh)
                            LoadStatus.FirstLoad(hasNoMore)
                        else
                            LoadStatus.LoadMore(hasNoMore),
                        homeMovesData = homeMovesList.list
                    )
                }
                homeMovesPageIndex++
            }
        }
    }

    /**
     * 获取首页电视剧数据
     */
    private fun getHomeTv(isRefresh: Boolean = false){
        if (isRefresh) {
            homeTvPageIndex = 1
        }
        rxRequest {
            onRequest = {
                val homeTvList = Net.getHomeTvData(homeTvPageIndex).await()
                val hasNoMore = homeTvPageIndex >= homeTvList.totalPage
                _viewStates.setState {
                    copy(
                        loadStatus = if (isRefresh)
                            LoadStatus.FirstLoad(hasNoMore)
                        else
                            LoadStatus.LoadMore(hasNoMore),
                        homeTvData = homeTvList.list
                    )
                }
                homeTvPageIndex++
            }
        }
    }

    /**
     * 获取首页动漫数据
     */
    private fun getHomeCartoon(isRefresh: Boolean = false){
        if (isRefresh) {
            homeCartoonIndex = 1
        }
        rxRequest {
            onRequest = {
                val homeCartoonList = Net.getHomeCartoonData(homeCartoonIndex).await()
                val hasNoMore = homeCartoonIndex >= homeCartoonList.totalPage
                _viewStates.setState {
                    copy(
                        loadStatus = if (isRefresh)
                            LoadStatus.FirstLoad(hasNoMore)
                        else
                            LoadStatus.LoadMore(hasNoMore),
                        homeCartoonData = homeCartoonList.list
                    )
                }
                homeCartoonIndex++
            }
        }
    }

    override val _viewStates: MutableLiveData<HomeViewState> = MutableLiveData(HomeViewState())
}