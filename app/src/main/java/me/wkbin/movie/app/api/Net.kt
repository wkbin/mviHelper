package me.wkbin.movie.app.api


import android.graphics.Bitmap
import rxhttp.toAwait
import rxhttp.wrapper.annotation.DefaultDomain
import rxhttp.wrapper.coroutines.Await
import rxhttp.wrapper.param.RxHttp.Companion.get
import rxhttp.wrapper.param.RxHttpNoBodyParam

object Net {

    @DefaultDomain
    const val BASE_URL = "http://mao.airuiclub.top"

    // 请求成功状态码
    const val REQUEST_SUC = 200

    private fun RxHttpNoBodyParam.toPageList(page: Int, size: Int = SIZE) = apply {
        add("pageNum", page)
        add("pageSize", size)
    }


    // 每页记录数
    private const val SIZE = 10

    /**
     * 首页推荐列表数据
     */
    fun getRecommendData(page: Int):Await<Bitmap> {
        return get("/video/vod/recommends")
            .toPageList(page)
            .toAwait()
    }

}