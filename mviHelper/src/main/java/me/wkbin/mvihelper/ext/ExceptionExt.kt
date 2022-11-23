package me.wkbin.mvihelper.ext

/**
 * 捕获异常返回空
 */
inline fun <T> tryOrNull(f: () -> T) =
    try {
        f()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
