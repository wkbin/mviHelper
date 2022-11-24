package me.wkbin.movie.app

import android.app.Application
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import dagger.hilt.android.HiltAndroidApp
import me.wkbin.movie.BuildConfig
import me.wkbin.movie.app.api.HttpLoggingInterceptor
import me.wkbin.mvihelper.module.MVIModule
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.converter.MoshiConverter
import rxhttp.wrapper.param.Param
import rxhttp.wrapper.ssl.HttpsUtils
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager
import javax.net.ssl.SSLSession


@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        MVIModule.init(this,true)
        init()
    }

    //建议在Application中调用
    private fun init() {
        // EXOPlayer内核
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        // exo 缓存模式 ，支持m3u8
        CacheFactory.setCacheManager(ExoPlayerCacheManager::class.java)
        // 显示

        val sslParams = HttpsUtils.getSslSocketFactory()
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //添加信任证书
            .hostnameVerifier { _: String?, _: SSLSession? -> true } //忽略host验证
            .build()
        RxHttpPlugins.init(client) //自定义OkHttpClient对象
            .setDebug(BuildConfig.DEBUG, false, 2) //调试模式/分段打印/json数据格式化输出
            .setOnParamAssembly { _: Param<*>? -> }
    }
}