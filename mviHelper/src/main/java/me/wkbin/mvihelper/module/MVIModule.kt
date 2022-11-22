package me.wkbin.mvihelper.module

import android.app.Application
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber


object MVIModule {

    private lateinit var app:Application
    private var isDebug = false

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    // 生命周期与Application一样长的协程，可以做一些后台作业
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate + errorHandler)

    private val packageInfo by lazy {
        val packageManager = app.packageManager
        val packageName = app.packageName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        }else{
            packageManager.getPackageInfo(packageName,0)
        }
    }

    @JvmStatic
    fun init(application: Application, isDebug: Boolean) {
        app = application
        this.isDebug = isDebug
        initTimber()
    }

    private fun initTimber() {
        if (isDebug) {
            val formatStrategy = PrettyFormatStrategy
                .newBuilder()
                .tag("Timber")
                .build()
            Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    Logger.log(priority, tag, message, t)
                }
            })
        }
    }


    @JvmStatic
    fun versionName(): String {
        return try {
            packageInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 获取应用程序名称
     */
    fun appName(): String {
        return kotlin.runCatching {
            val labelRes = packageInfo.applicationInfo.labelRes
            app.resources.getString(labelRes)
        }.getOrElse {
            it.printStackTrace()
            ""
        }
    }

    /**
     * 获取应用包名
     */
    fun packageName(): String {
        return kotlin.runCatching {
            packageInfo.packageName
        }.getOrElse {
            it.printStackTrace()
            ""
        }
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    fun phoneType(): String {
        return Build.MODEL ?: ""
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    fun systemVersion(): String {
        return Build.VERSION.RELEASE ?: ""
    }

    /**
     * 获取屏幕宽高, first 是宽  second 是高
     */
    fun getScreenInfo(): Pair<Int, Int> {
        return Pair(
            Resources.getSystem().displayMetrics.widthPixels,
            Resources.getSystem().displayMetrics.heightPixels
        )
    }

    @JvmStatic
    fun application() = app

    @JvmStatic
    fun isDebug() = isDebug
}