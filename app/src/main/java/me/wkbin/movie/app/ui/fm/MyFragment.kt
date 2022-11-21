package me.wkbin.movie.app.ui.fm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import me.wkbin.movie.R
import me.wkbin.movie.app.ui.mvi.DefaultEffect
import me.wkbin.movie.app.ui.mvi.DefaultState
import me.wkbin.movie.app.ui.mvi.DefaultViewEvent
import me.wkbin.movie.app.ui.vm.MyVM
import me.wkbin.movie.databinding.FragmentMyBinding
import me.wkbin.mvihelper.base.BaseVBFragment


class MyFragment : BaseVBFragment<DefaultEffect, DefaultViewEvent, DefaultState,MyVM,FragmentMyBinding>() {

    companion object {
        fun newInstance() = MyFragment()
    }

    override val mViewModel: MyVM by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun renderViewState(viewStates: LiveData<DefaultEffect>) {

    }


}