package me.wkbin.movie


import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import me.wkbin.movie.app.ui.mvi.FetchStatus
import me.wkbin.movie.app.ui.mvi.MainViewEffect
import me.wkbin.movie.app.ui.mvi.MainViewEvent
import me.wkbin.movie.app.ui.mvi.MainViewState
import me.wkbin.movie.app.ui.vm.MainVM
import me.wkbin.movie.databinding.ActivityMainBinding
import me.wkbin.mvihelper.base.BaseVBActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BaseVBActivity<MainViewState, MainViewEffect, MainViewEvent, MainVM, ActivityMainBinding>() {

    override val mViewModel: MainVM by viewModels()


    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.btnShowToast.setOnClickListener {
            mViewModel.shoToast()
        }

        mViewBind.btnSwipe.setOnClickListener {
            mViewModel.process(MainViewEvent.OnSwipeRefresh)
        }

        mViewBind.btnGet.setOnClickListener {
            mViewModel.loadData()
        }
    }

    override fun renderViewEffect(viewEffect: MainViewEffect) {

    }

    override fun renderViewState(viewState: MainViewState) {
        when (viewState.fetchStatus) {
            is FetchStatus.Fetched -> {
                mViewBind.iv.setImageBitmap(viewState.bitmap)
            }

            is FetchStatus.NotFetched -> {

            }

            is FetchStatus.Fetching -> {

            }
        }
    }


}