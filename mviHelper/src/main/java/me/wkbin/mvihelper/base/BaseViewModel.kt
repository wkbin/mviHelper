package me.wkbin.mvihelper.base


import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.wkbin.mvihelper.core.UiEffect
import me.wkbin.mvihelper.core.ViewModelContract
import me.wkbin.mvihelper.core.livedata.SingleLiveEvent
import me.wkbin.mvihelper.exception.NoObserverAttachedException
import me.wkbin.mvihelper.ext.TAG
import me.wkbin.mvihelper.ext.setState
import timber.log.Timber

abstract class BaseViewModel<STATE,EVENT> :
     ViewModel(), ViewModelContract<EVENT> {

    protected abstract val _viewStates: MutableLiveData<STATE>
    fun viewStates(): LiveData<STATE> = _viewStates


    @CallSuper
    override fun process(viewEvent: EVENT) {
        Timber.d(TAG, "processing viewEvent: $viewEvent")
    }

    private val _uiEffects: SingleLiveEvent<UiEffect> = SingleLiveEvent()
    fun uiEffects(): SingleLiveEvent<UiEffect> = _uiEffects

    private var _uiEffect: UiEffect? = null
    var uiEffect: UiEffect
        get() = _uiEffect
            ?: throw UninitializedPropertyAccessException("\"uiEffect\" was queried before being initialized")
        set(value) {
            Timber.d(TAG, "setting uiEffect : $value")
            _uiEffect = value
            _uiEffects.value = value
        }

    override fun onCleared() {
        super.onCleared()
        Timber.d(TAG, "onCleared")
    }

}