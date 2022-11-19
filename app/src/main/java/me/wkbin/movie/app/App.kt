package me.wkbin.movie.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import me.wkbin.movie.BuildConfig
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.converter.MoshiConverter
import rxhttp.wrapper.param.Param
import rxhttp.wrapper.ssl.HttpsUtils
import javax.net.ssl.SSLSession


@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        init()
    }

    //建议在Application中调用
    private fun init() {
        val sslParams = HttpsUtils.getSslSocketFactory()
        val client: OkHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //添加信任证书
            .hostnameVerifier { _: String?, _: SSLSession? -> true } //忽略host验证
            .build()
        RxHttpPlugins.init(client) //自定义OkHttpClient对象
            .setConverter(MoshiConverter.create())
            .setDebug(BuildConfig.DEBUG, false, 2) //调试模式/分段打印/json数据格式化输出
            .setOnParamAssembly { _: Param<*>? -> }
    }
}