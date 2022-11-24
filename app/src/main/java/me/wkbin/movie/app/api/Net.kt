package me.wkbin.movie.app.api


import VideoDetailData
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
    const val REQUEST_SUC = 0

    // 每页记录数
    private const val SIZE = 5

    private fun RxHttpNoBodyParam.toPageList(page: Int, size: Int = SIZE) = apply {
        add("pageNum", page)
        add("pageSize", size)
    }


    /**
     * 首页推荐列表数据
     */
    fun getRecommendData(page: Int): Await<ApiPagerResponse<HomeData>> {
        return RxHttp.get("video/vod/recommends")
            .toPageList(page)
            .toAwaitResponse()
    }

    /**
     * 首页电影
     */
    fun getHomeMovesData(page: Int): Await<ApiPagerResponse<HomeData>> {
        return RxHttp.get("video/vod/moves")
            .toPageList(page)
            .toAwaitResponse()
    }

    /**
     * 首页电视剧
     */
    fun getHomeTvData(page: Int): Await<ApiPagerResponse<HomeData>> {
        return RxHttp.get("video/vod/tvs")
            .toPageList(page)
            .toAwaitResponse()
    }

    /**
     * 首页动漫
     */
    fun getHomeCartoonData(page: Int): Await<ApiPagerResponse<HomeData>> {
        return RxHttp.get("video/vod/anims")
            .toPageList(page)
            .toAwaitResponse()
    }

    /**
     * 查看视频详情
     */
    fun getVideoDetailData(
        videoId: String,
        deviceId: String,
        userId: String?
    ): Await<VideoDetailData> {
        return RxHttp.get("video/vod/detail")
            .add("id", videoId)
            .add("deviceId", deviceId)
            .add("userId", userId, userId != null)
            .toAwaitResponse()
    }

}