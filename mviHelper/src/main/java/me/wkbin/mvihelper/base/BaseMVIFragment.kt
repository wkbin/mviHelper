package me.wkbin.mvihelper.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import me.wkbin.mvihelper.core.UiEffect
import me.wkbin.mvihelper.ext.dismissLoadingExt
import me.wkbin.mvihelper.ext.showLoadingExt
import me.wkbin.mvihelper.ext.toast

abstract class BaseMVIFragment<STATE, EVENT, VM : BaseViewModel<STATE, EVENT>>:Fragment() {

    abstract val mViewModel: VM

    // 使用ViewBinding后该变量会赋值
    private var viewBinding: View? = null

    // 不使用ViewBinding需要指定布局id
    @get:LayoutRes
    abstract val layoutId: Int

    //是否第一次加载
    private var isFirst = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isFirst = true
        viewBinding = initViewBind(inflater,container)
        return viewBinding?:(inflater.inflate(layoutId,container,false))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        beforeCreate()
        super.onViewCreated(view, savedInstanceState)
        registerUiChange()
        view.post {
            initView(savedInstanceState)
        }
    }


    override fun onResume() {
        super.onResume()
        onVisible()
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            view?.post {
                lazyLoadData()
                isFirst = false
            }
        }
    }

    private fun registerUiChange() {
        renderViewState(mViewModel.viewStates())
        mViewModel.uiEffects().observe(viewLifecycleOwner, uiEffectObserver)
    }

    /**
     * 懒加载
     */
    open fun lazyLoadData() {}

    /**
     *  执行在 initView 之前，
     */
    open fun beforeCreate() {}

    /**
     * 使用ViewBinding后该方法会进行初始化
     */
    open fun initViewBind(inflater: LayoutInflater, container: ViewGroup?): View? = null


    /**
     * 初始化View
     */
    abstract fun initView(savedInstanceState: Bundle?)


    abstract fun renderViewState(viewStates: LiveData<STATE>)


    private val uiEffectObserver = Observer<UiEffect> { uiEffect ->
        when (uiEffect) {
            is UiEffect.ShowToast -> toast(uiEffect.message)
            is UiEffect.ShowLoadDialog -> showLoadingExt(uiEffect.message)
            is UiEffect.DismissLoadDialog -> dismissLoadingExt()
        }
    }

}