package me.wkbin.movie.app.ui.vm

import androidx.lifecycle.MutableLiveData
import me.wkbin.mvihelper.core.DefaultEffect
import me.wkbin.mvihelper.core.DefaultState
import me.wkbin.mvihelper.core.DefaultViewEvent
import me.wkbin.mvihelper.base.BaseViewModel

class MyVM: BaseViewModel<DefaultEffect, DefaultState>() {
    override val _viewStates: MutableLiveData<DefaultEffect>
        get() = MutableLiveData()
}