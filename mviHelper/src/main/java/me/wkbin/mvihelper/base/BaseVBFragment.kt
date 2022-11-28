package me.wkbin.mvihelper.base

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.ViewBindingUtil

abstract class BaseVBFragment<STATE, EVENT, VM : BaseViewModel<STATE, EVENT>, VB : ViewBinding>
    : BaseMVIFragment<STATE, EVENT, VM>() {

    // 使用了ViewBinding 就不需要layoutId
    override val layoutId: Int
        get() = 0

    private var _binding: VB? = null
    protected val mViewBind: VB get() = _binding!!
    private val handle by lazy { Handler(Looper.getMainLooper()) }

    override fun initViewBind(inflater: LayoutInflater, container: ViewGroup?): View? {
        if (_binding == null) {
            _binding = ViewBindingUtil.inflateWithGeneric(this, inflater)
            viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    handle.post {
                        _binding = null
                    }
                }
            })
        }
        return mViewBind.root
    }
}