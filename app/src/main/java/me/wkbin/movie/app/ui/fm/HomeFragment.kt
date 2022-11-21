package me.wkbin.movie.app.ui.fm

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import me.wkbin.movie.app.ui.mvi.*
import me.wkbin.movie.app.ui.vm.HomeVM
import me.wkbin.movie.databinding.FragmentHomeBinding
import me.wkbin.mvihelper.base.BaseVBFragment


class HomeFragment : BaseVBFragment<HomeViewState,DefaultEffect,HomeViewEvent,HomeVM,FragmentHomeBinding>() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override val mViewModel: HomeVM by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun renderViewState(viewStates: LiveData<HomeViewState>) {

    }
}