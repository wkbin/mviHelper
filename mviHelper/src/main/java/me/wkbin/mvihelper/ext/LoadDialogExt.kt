package me.wkbin.mvihelper.ext

import android.app.Dialog
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import me.wkbin.mvihelper.R


private var loadingDialog:Dialog? = null


fun Fragment.showLoadingExt(message:String = ""){
    (activity as? AppCompatActivity)?.showLoadingExt(message)
}

fun Fragment.dismissLoadingExt(){
    (activity as? AppCompatActivity)?.dismissLoadingExt()
}


fun AppCompatActivity.showLoadingExt(message:String = ""){
    dismissLoadingExt()
    if(!isFinishing){
        val layout = LayoutInflater.from(this@showLoadingExt)
            .inflate(R.layout.layout_loading_view, null)
        val loadingTips = layout.findViewById<TextView>(R.id.loading_tips)
        loadingTips.text = message
        if(loadingDialog == null){
            loadingDialog = Dialog(this , R.style.loadingDialogTheme).apply {
                setCancelable(true)
                setCanceledOnTouchOutside(false)
                setContentView(layout)
                setOnDismissListener {
                    dismissLoadingExt()
                }
            }
        }
        loadingDialog?.setContentView(layout)
        loadingDialog?.show()
    }
}


fun AppCompatActivity.dismissLoadingExt(){
    loadingDialog?.dismiss()
    loadingDialog = null
}