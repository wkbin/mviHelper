package me.wkbin.movie.app.ui.vm

import androidx.lifecycle.MutableLiveData
import me.wkbin.mvihelper.base.BaseViewModel
import me.wkbin.mvihelper.core.DefaultEffect
import me.wkbin.mvihelper.core.DefaultState
import me.wkbin.mvihelper.core.DefaultViewEvent

class MainVM:BaseViewModel<DefaultState, DefaultViewEvent>() {
    override val _viewStates: MutableLiveData<DefaultState>
        get() = MutableLiveData()
}