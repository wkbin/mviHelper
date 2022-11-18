package me.wkbin.mvihelper.ext

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.toast(message: String, len: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, len).show()
}

fun Fragment.toast(message: String, len: Int = Toast.LENGTH_SHORT) {
    (activity as? AppCompatActivity)?.toast(message, len)
}