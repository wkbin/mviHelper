package me.wkbin.movie


import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import dagger.hilt.android.AndroidEntryPoint
import me.wkbin.movie.app.ui.adapter.MainAdapter
import me.wkbin.mvihelper.core.DefaultEffect
import me.wkbin.movie.app.ui.mvi.HomeViewEvent
import me.wkbin.movie.app.ui.mvi.HomeViewState
import me.wkbin.movie.app.ui.vm.HomeVM
import me.wkbin.movie.databinding.ActivityMainBinding
import me.wkbin.mvihelper.base.BaseVBActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BaseVBActivity<HomeViewState, DefaultEffect, HomeViewEvent, HomeVM, ActivityMainBinding>(){

    @Inject
    lateinit var vpAdapter:MainAdapter

    override val mViewModel: HomeVM by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.vpMain.apply {
            adapter = vpAdapter
            isUserInputEnabled = false
        }
        mViewBind.navView.setOnItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.nav_movie -> {
                    mViewBind.vpMain.currentItem = 0
                    true
                }
                R.id.nav_my -> {
                    mViewBind.vpMain.currentItem = 1
                    true
                }
                else -> false
            }
        }
        mViewBind.vpMain.registerOnPageChangeCallback(object :OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0){
                    mViewBind.navView.menu.findItem(R.id.nav_movie).isChecked = true
                }else{
                    mViewBind.navView.menu.findItem(R.id.nav_my).isChecked = true
                }
            }
        })
    }


    override fun renderViewState(viewStates: LiveData<HomeViewState>) {}


}