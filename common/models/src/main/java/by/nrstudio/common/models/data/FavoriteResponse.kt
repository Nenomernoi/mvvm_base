package by.nrstudio.common.models.data

import com.squareup.moshi.Json

data class FavoriteResponse(
    @field:Json(name = "created_at") val createdAt: String,
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "image") val image: ImageShortResponse,
    @field:Json(name = "image_id") val imageId: String,
    @field:Json(name = "sub_id") val subId: String,
    @field:Json(name = "user_id") val userId: String
)

data class ImageShortResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "url") val url: String
)

data class FavoriteAddedSuccessResponse(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "message") val message: String
)

data class FavoriteRemoveSuccessResponse(
    @field:Json(name = "message") val message: String
)

data class FavoriteRequest(
    @field:Json(name = "image_id") val id: String,
    @field:Json(name = "sub_id") val userUuid: String = "test-user-mvi"
)
