package me.wkbin.mvihelper.core

sealed class UiEffect {
    /**
     * 显示Toast
     */
    data class ShowToast(val message: String) : UiEffect()

    /**
     * 显示对话框
     */
    data class ShowLoadDialog(val message: String) : UiEffect()

    /**
     * 关闭对话框
     */
    object DismissLoadDialog : UiEffect()
}