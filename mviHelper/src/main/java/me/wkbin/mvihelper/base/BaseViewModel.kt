package me.wkbin.mvihelper.base

import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.wkbin.mvihelper.core.UiEffect
import me.wkbin.mvihelper.core.ViewModelContract
import me.wkbin.mvihelper.core.livedata.SingleLiveEvent
import me.wkbin.mvihelper.exception.NoObserverAttachedException
import me.wkbin.mvihelper.ext.TAG

open class BaseViewModel<STATE , EFFECT , EVENT> : ViewModel(),
    ViewModelContract<EVENT> {

    private val _viewStates: MutableLiveData<STATE> = MutableLiveData()
    fun viewStates(): LiveData<STATE> = _viewStates

    private var _viewState: STATE? = null
    protected var viewState: STATE
        get() = _viewState
            ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
        set(value) {
            Log.d(TAG, "setting viewState : $value")
            _viewState = value
            _viewStates.value = value!!
        }


    private val _viewEffects: SingleLiveEvent<EFFECT> = SingleLiveEvent()
    fun viewEffects(): SingleLiveEvent<EFFECT> = _viewEffects

    private var _viewEffect: EFFECT? = null
    protected var viewEffect: EFFECT
        get() = _viewEffect
            ?: throw UninitializedPropertyAccessException("\"viewEffect\" was queried before being initialized")
        set(value) {
            Log.d(TAG, "setting viewEffect : $value")
            _viewEffect = value
            _viewEffects.value = value!!
        }


    @CallSuper
    override fun process(viewEvent: EVENT) {
        if (!viewStates().hasObservers()) {
            throw NoObserverAttachedException("No observer attached. In case of AacMviCustomView \"startObserving()\" function needs to be called manually.")
        }
        Log.d(TAG, "processing viewEvent: $viewEvent")
    }

    private val _uiEffects:SingleLiveEvent<UiEffect> = SingleLiveEvent()
    fun uiEffects():SingleLiveEvent<UiEffect> = _uiEffects

    private var _uiEffect:UiEffect? = null
    var uiEffect:UiEffect
        get() = _uiEffect?:throw UninitializedPropertyAccessException("\"uiEffect\" was queried before being initialized")
        set(value){
            Log.d(TAG, "setting uiEffect : $value")
            _uiEffect = value
            _uiEffects.value = value
        }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }

}