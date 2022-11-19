package me.wkbin.movie


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import me.wkbin.movie.app.ui.adapter.MainAdapter
import me.wkbin.movie.app.ui.mvi.DefaultEffect
import me.wkbin.movie.app.ui.mvi.MainViewEvent
import me.wkbin.movie.app.ui.mvi.MainViewState
import me.wkbin.movie.app.ui.vm.MainVM
import me.wkbin.movie.databinding.ActivityMainBinding
import me.wkbin.mvihelper.base.BaseVBActivity
import me.wkbin.mvihelper.ext.observeState
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BaseVBActivity<MainViewState, DefaultEffect, MainViewEvent, MainVM, ActivityMainBinding>(){

    @Inject
    lateinit var vpAdapter:MainAdapter

    override val mViewModel: MainVM by viewModels()




    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.vpMain.adapter = vpAdapter
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


    override fun renderViewState(viewStates: LiveData<MainViewState>) {}


}