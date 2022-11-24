
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class VideoDetailData(
    @Json(name = "actors")
    val actors: String?,
    @Json(name = "area")
    val area: String?,
    @Json(name = "blurb")
    val blurb: String?,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "pic")
    val pic: String,
    @Json(name = "playMedias")
    val playMedias: List<PlayMedia>,
    @Json(name = "progress")
    val progress: Int,
    @Json(name = "type")
    val type: String?,
    @Json(name = "year")
    val year: String?
)

@JsonClass(generateAdapter = true)
data class PlayMedia(
    @Json(name = "name")
    val name: String,
    @Json(name = "playUrl")
    val playUrl: String
)