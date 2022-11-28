package me.wkbin.movie.app.ui.fm

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.loadState.LoadState
import dagger.hilt.android.AndroidEntryPoint
import me.wkbin.movie.app.data.HomeData
import me.wkbin.movie.app.ext.loadMore
import me.wkbin.movie.app.ui.act.VideoDetailActivity
import me.wkbin.movie.app.ui.adapter.BannerAdapter
import me.wkbin.movie.app.ui.adapter.MovieAdapter
import me.wkbin.movie.app.ui.mvi.*
import me.wkbin.movie.app.ui.vm.HomeVM
import me.wkbin.movie.app.util.IntentKey
import me.wkbin.mvihelper.util.DefaultDivider
import me.wkbin.movie.databinding.FragmentHomeBinding
import me.wkbin.mvihelper.base.BaseVBFragment
import me.wkbin.mvihelper.core.DefaultEffect
import me.wkbin.mvihelper.ext.divider
import me.wkbin.mvihelper.ext.dp
import me.wkbin.mvihelper.ext.horizontal
import me.wkbin.mvihelper.ext.observeState
import me.wkbin.mvihelper.ext.startActivity
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment :
    BaseVBFragment<HomeViewState, HomeViewEvent, HomeVM, FragmentHomeBinding>() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override val mViewModel: HomeVM by viewModels()

    @Inject
    lateinit var bannerAdapter: BannerAdapter

    @Inject
    lateinit var recommendAdapter: MovieAdapter
    private val recommendHelper by lazy {
        recommendAdapter.loadMore {
            onLoadOrFail = {
                mViewModel.process(HomeViewEvent.OnLoadMore(HomeViewEventType.Recommend))
            }
        }
    }

    @Inject
    lateinit var homeMovieAdapter: MovieAdapter
    private val homeMovieHelper by lazy {
        homeMovieAdapter.loadMore {
            onLoadOrFail = {
                mViewModel.process(HomeViewEvent.OnLoadMore(HomeViewEventType.Movie))
            }
        }
    }

    @Inject
    lateinit var tVDramaAdapter: MovieAdapter
    private val tvDramaHelper by lazy {
        tVDramaAdapter.loadMore {
            onLoadOrFail = {
                mViewModel.process(HomeViewEvent.OnLoadMore(HomeViewEventType.TVDrama))
            }
        }
    }

    @Inject
    lateinit var cartoonAdapter: MovieAdapter
    private val cartoonHelper by lazy {
        cartoonAdapter.loadMore {
            onLoadOrFail = {
                mViewModel.process(HomeViewEvent.OnLoadMore(HomeViewEventType.Cartoon))
            }
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        setBannerData()
        mViewBind.rcRecommend.init(recommendHelper.adapter)
        mViewBind.rcMovie.init(homeMovieHelper.adapter)
        mViewBind.rcTVDrama.init(tvDramaHelper.adapter)
        mViewBind.rcCartoon.init(cartoonHelper.adapter)
        recommendAdapter.setOnItemClickListener(adapterClickListener)
        homeMovieAdapter.setOnItemClickListener(adapterClickListener)
        tVDramaAdapter.setOnItemClickListener(adapterClickListener)
        cartoonAdapter.setOnItemClickListener(adapterClickListener)
        mViewModel.process(HomeViewEvent.OnSwipeRefresh)


    }

    private val adapterClickListener =
        BaseQuickAdapter.OnItemClickListener<HomeData> { adapter, _, position ->
            val item = adapter.getItem(position)
            context?.startActivity<VideoDetailActivity>(
                bundleOf(IntentKey.VIDEO_ID to item?.id)
            )
        }


    private fun setBannerData() {
        mViewBind.banner.apply {
            disallowParentInterceptDownEvent(true)
            setLifecycleRegistry(lifecycle)
            setAdapter(bannerAdapter)
            create(mViewModel.mockBanner)
        }
    }

    /**
     * recycleView初始化扩展
     */
    private fun RecyclerView.init(adapter: RecyclerView.Adapter<*>) {
        this.adapter = adapter
        horizontal()
        divider {
            mDividerSize = 5.dp
            mIncludeSize = 16.dp
            mOrientation = RecyclerView.HORIZONTAL
        }
    }


    override fun renderViewState(viewStates: LiveData<HomeViewState>) {
        viewStates.run {
            observeState(
                viewLifecycleOwner,
                HomeViewState::loadStatus,
                HomeViewState::recommendData
            ) { status, data ->
                when (status) {
                    is LoadStatus.FirstLoad -> {
                        recommendAdapter.submitList(data)
                        recommendHelper.trailingLoadState = LoadState.NotLoading(status.hasNoMore)
                    }

                    is LoadStatus.LoadMore -> {
                        recommendAdapter.addAll(data!!.toMutableList())
                        recommendHelper.trailingLoadState = LoadState.NotLoading(status.hasNoMore)
                    }
                }
            }
            observeState(
                viewLifecycleOwner,
                HomeViewState::loadStatus,
                HomeViewState::homeMovesData
            ) { status, data ->
                when (status) {
                    is LoadStatus.FirstLoad -> {
                        homeMovieAdapter.submitList(data)
                        homeMovieHelper.trailingLoadState = LoadState.NotLoading(status.hasNoMore)
                    }

                    is LoadStatus.LoadMore -> {
                        homeMovieAdapter.addAll(data!!.toMutableList())
                        homeMovieHelper.trailingLoadState = LoadState.NotLoading(status.hasNoMore)
                    }
                }
            }
            observeState(
                viewLifecycleOwner,
                HomeViewState::loadStatus,
                HomeViewState::homeTvData
            ) { status, data ->
                when (status) {
                    is LoadStatus.FirstLoad -> {
                        tVDramaAdapter.submitList(data)
                        tvDramaHelper.trailingLoadState = LoadState.NotLoading(status.hasNoMore)
                    }

                    is LoadStatus.LoadMore -> {
                        tVDramaAdapter.addAll(data!!.toMutableList())
                        tvDramaHelper.trailingLoadState = LoadState.NotLoading(status.hasNoMore)
                    }
                }
            }
            observeState(
                viewLifecycleOwner,
                HomeViewState::loadStatus,
                HomeViewState::homeCartoonData
            ) { status, data ->
                when (status) {
                    is LoadStatus.FirstLoad -> {
                        cartoonAdapter.submitList(data)
                        cartoonHelper.trailingLoadState = LoadState.NotLoading(status.hasNoMore)
                    }

                    is LoadStatus.LoadMore -> {
                        cartoonAdapter.addAll(data!!.toMutableList())
                        cartoonHelper.trailingLoadState = LoadState.NotLoading(status.hasNoMore)
                    }
                }
            }
        }
    }
}