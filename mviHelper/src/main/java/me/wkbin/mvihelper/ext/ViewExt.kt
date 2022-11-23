package me.wkbin.mvihelper.ext

import android.content.res.Resources
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding



/**
 * 将传入的Int类型转换为需要的像素值
 */
val Int.dp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/**
 * 将传入的Float类型转换为需要的像素值
 */
val Float.dp: Float
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

/**
 * 将传入的Float类型转换为需要的像素值
 */
val Float.sp: Float
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)

/**
 * 将传入的Float类型转换为需要的像素值
 */
val Int.sp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

abstract class OnSingleClickListener(val duration: Int) : View.OnClickListener {
    private var lastClickTime = -1L

    final override fun onClick(view: View) {
        lastClickTime = if (lastClickTime < 0) {
            onSingleClick(view)
            System.currentTimeMillis()
        } else {
            val now = System.currentTimeMillis()
            if (now - lastClickTime > duration) {
                onSingleClick(view)
            }

            now
        }
    }

    /**
     * 点击操作回调。
     *
     * @param view 当前点击操作的视图
     */
    abstract fun onSingleClick(view: View)
}

/**
 * 设置单次点击事件监听器，避免连续快速点击造成两次重复操作。
 *
 * @param duration 点击时间间隔
 * @param onSingleClick 点击事件回调，快速连续点击将被忽略掉
 */
fun View.setOnSingleClickListener(duration: Int = 500, onSingleClick: (view: View) -> Unit) {
    setOnClickListener(object: OnSingleClickListener(duration) {
        override fun onSingleClick(view: View) {
            onSingleClick.invoke(view)
        }
    })
}

/**
 * 简化视图绑定的扩展方法
 */
fun <T : ViewDataBinding> View.bind() = DataBindingUtil.bind<T>(this) as T
