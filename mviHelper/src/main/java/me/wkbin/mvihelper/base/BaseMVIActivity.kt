package me.wkbin.mvihelper.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import me.wkbin.mvihelper.R
import me.wkbin.mvihelper.core.UiEffect
import me.wkbin.mvihelper.ext.dismissLoadingExt
import me.wkbin.mvihelper.ext.showLoadingExt
import me.wkbin.mvihelper.ext.toast

/**
 * 只使用ViewModel继承它
 */
abstract class BaseMVIActivity<STATE, EFFECT, EVENT, VM : BaseViewModel<STATE, EFFECT, EVENT>> :
    AppCompatActivity() {

    abstract val mViewModel: VM

    // 可以按自己定义的 Toolbar
    private var mTitleBarView: View? = null

    // 使用ViewBinding后该变量会赋值
    private var viewBinding: View? = null

    // 不使用ViewBinding需要指定布局id
    @get:LayoutRes
    abstract val layoutId: Int

    private val baseRootView by lazy { findViewById<LinearLayout>(R.id.baseRootView) }
    private val baseContentView by lazy { findViewById<FrameLayout>(R.id.baseContentView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeCreate()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        //注册事件监听
        registerUiChange()
        //初始化 status View
        initStatusView(savedInstanceState)
    }

    private fun registerUiChange() {
        renderViewState(mViewModel.viewStates())
        mViewModel.viewEffects().observe(this, viewEffectObserver)
        mViewModel.uiEffects().observe(this, uiEffectObserver)
    }


    private val viewEffectObserver = Observer<EFFECT> {
        renderViewEffect(it)
    }

    private val uiEffectObserver = Observer<UiEffect> { uiEffect ->
        when (uiEffect) {
            is UiEffect.ShowToast -> toast(uiEffect.message)
            is UiEffect.ShowLoadDialog -> showLoadingExt(uiEffect.message)
            is UiEffect.DismissLoadDialog -> dismissLoadingExt()
        }
    }

    /**
     * 初始化标题栏
     */
    private fun initStatusView(savedInstanceState: Bundle?) {
        viewBinding = initViewBind()
        mTitleBarView = createTitleBarView()
        initImmersionBar()
        (viewBinding ?: LayoutInflater.from(this).inflate(layoutId, null))?.let {
            baseContentView.addView(it)
        }
        initView(savedInstanceState)
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected open fun initImmersionBar() {
        ImmersionBar.with(this, false).apply { setImmersionBarBlock(this)  }.init()
    }



    open fun setImmersionBarBlock(immersionBar:ImmersionBar){
        if (mTitleBarView == null){
            immersionBar.fitsSystemWindows(true)
            immersionBar.statusBarDarkFont(isDarkFontForStatusBar())
        }else{
            immersionBar.statusBarDarkFont(isDarkFontForStatusBar())
            immersionBar.titleBar(mTitleBarView)
        }
    }

    /**
     * 状态栏默认黑色字体
     */
    open fun isDarkFontForStatusBar() = true

    /**
     * onCreate之前调用
     */
    open fun beforeCreate() {}

    /**
     * 使用ViewBinding后该方法会进行初始化
     */
    open fun initViewBind(): View? = null

    /**
     * 是否显示标题栏 默认显示
     */
    open fun showToolBar() = true

    /**
     * 子类可传入自己的定义标题栏，默认不显示标题栏
     */
    open fun createTitleBarView(): View? = null

    /**
     * 初始化View
     */
    abstract fun initView(savedInstanceState: Bundle?)


    abstract fun renderViewState(viewStates: LiveData<STATE>)


    open fun renderViewEffect(viewEffect: EFFECT){}
}