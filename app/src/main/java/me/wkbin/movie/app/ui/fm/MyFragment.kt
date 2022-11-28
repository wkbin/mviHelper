package me.wkbin.movie.app.ui.fm

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import me.wkbin.mvihelper.core.DefaultEffect
import me.wkbin.mvihelper.core.DefaultState
import me.wkbin.mvihelper.core.DefaultViewEvent
import me.wkbin.movie.app.ui.vm.MyVM
import me.wkbin.movie.databinding.FragmentMyBinding
import me.wkbin.mvihelper.base.BaseVBFragment


class MyFragment : BaseVBFragment<DefaultEffect, DefaultState, MyVM, FragmentMyBinding>() {

    companion object {
        fun newInstance() = MyFragment()
    }

    override val mViewModel: MyVM by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun renderViewState(viewStates: LiveData<DefaultEffect>) {

    }


}