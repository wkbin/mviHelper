package me.wkbin.mvihelper.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

/**
 * 打开键盘
 */
fun EditText.openKeyboard() {
    this.apply {
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
    }
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, 0)
}

/**
 * 关闭键盘焦点
 */
fun Activity.hideOffKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive && this.currentFocus != null) {
        if (this.currentFocus?.windowToken != null) {
            imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}


/**
 * 为 Activity 类获取新 Intent 的扩展方法
 */
inline fun <reified T : Any> Context.intent() = Intent(this, T::class.java)


/**
 * Context 的 startActivity 的扩展方法。
 */
inline fun <reified T : Activity> Context?.startActivity(bundle: Bundle? = null) =
    this?.startActivity(Intent(this, T::class.java).apply {
        bundle?.let { putExtras(it) }
    })

inline fun <reified T : Activity> Fragment.startActivity(bundle: Bundle? = null) =
    context.startActivity<T>(bundle)

inline fun <reified T : Activity> Activity.startActivity(bundle: Bundle? = null) =
    this.startActivity(Intent(this, T::class.java).apply {
        bundle?.let { putExtras(it) }
    })

inline fun <reified T> Activity.lazyIntentExtra(key: String, defaultValue: T? = null) = lazy {
    (intent.extras?.get(key) as? T) ?: defaultValue
}

// 断言非空
inline fun <reified T> Activity.lazyAssertIntentExtra(key: String) = lazy {
    intent.extras?.get(key) as T
}

inline fun <reified T> Fragment.lazyArgument(key: String, defaultValue: T? = null) = lazy {
    (arguments?.get(key) as? T) ?: defaultValue
}

// 断言非空
inline fun <reified T> Fragment.lazyAssertIntentExtra(key: String) = lazy {
    arguments?.get(key) as T
}
