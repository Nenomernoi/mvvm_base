package org.base.network.models.base

import com.squareup.moshi.Json

data class ResponseError(
    @field:Json(name = "status_message") val message: String,
    @field:Json(name = "status_code") val status: Int
)