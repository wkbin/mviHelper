package me.wkbin.movie.app.api


import me.wkbin.movie.app.data.HomeData
import rxhttp.wrapper.annotation.DefaultDomain
import rxhttp.wrapper.coroutines.Await
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.RxHttpNoBodyParam
import rxhttp.wrapper.param.toAwaitResponse

object Net {

    @DefaultDomain
    const val BASE_URL = "http://mao.airuiclub.top/"

    // 请求成功状态码
    const val REQUEST_SUC = 200

    // 每页记录数
    private const val SIZE = 10

    private fun RxHttpNoBodyParam.toPageList(page: Int, size: Int = SIZE) = apply {
        add("pageNum", page)
        add("pageSize", size)
    }


    /**
     * 首页推荐列表数据
     */
    fun getRecommendData(page: Int): Await<List<HomeData>> {
        return RxHttp.get("video/vod/recommends")
            .toPageList(page)
            .toAwaitResponse()
    }

    /**
     * 首页电影
     */
    fun getHomeMovesData(page:Int):Await<List<HomeData>>{
        return RxHttp.get("video/vod/moves")
            .toPageList(page)
            .toAwaitResponse()
    }

}