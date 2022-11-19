package me.wkbin.movie


import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import dagger.hilt.android.AndroidEntryPoint
import me.wkbin.movie.app.ui.mvi.DefaultEffect
import me.wkbin.movie.app.ui.mvi.MainViewEvent
import me.wkbin.movie.app.ui.mvi.MainViewState
import me.wkbin.movie.app.ui.vm.MainVM
import me.wkbin.movie.databinding.ActivityMainBinding
import me.wkbin.mvihelper.base.BaseVBActivity
import me.wkbin.mvihelper.ext.observeState

@AndroidEntryPoint
class MainActivity :
    BaseVBActivity<MainViewState, DefaultEffect, MainViewEvent, MainVM, ActivityMainBinding>(){


    override val mViewModel: MainVM by viewModels()


    override fun initView(savedInstanceState: Bundle?) {
//        mViewBind.btnShowToast.setOnClickListener {
//            mViewModel.shoToast()
//        }
//
//        mViewBind.btnSwipe.setOnClickListener {
//            mViewModel.process(MainViewEvent.OnSwipeRefresh)
//        }
//
//        mViewBind.btnGet.setOnClickListener {
//            mViewModel.loadData()
//        }
    }

    override fun renderViewState(viewStates: LiveData<MainViewState>) {
        viewStates.run {
            observeState(this@MainActivity,MainViewState::bitmap){
//                mViewBind.iv.setImageBitmap(it)
            }
            observeState(this@MainActivity,MainViewState::recommendData){
                Log.d("renderViewState",it.toString())
            }
        }
    }


}