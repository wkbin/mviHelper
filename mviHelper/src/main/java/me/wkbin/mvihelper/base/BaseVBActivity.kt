package me.wkbin.mvihelper.base

import android.view.View
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.ViewBindingUtil

/**
 * 需要使用ViewBinding继承它
 */
abstract class BaseVBActivity<STATE, EVENT, VM : BaseViewModel<STATE, EVENT>, VB : ViewBinding> :
    BaseMVIActivity<STATE,EVENT, VM>() {
    // 使用了ViewBinding 就不需要layoutId
    override val layoutId: Int
        get() = 0

    protected lateinit var mViewBind: VB

    override fun initViewBind(): View? {
        mViewBind = ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
        return mViewBind.root
    }
}