package org.base.breed_data.data_source.remote.retrofit_service

import org.base.common.models.data.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesService {

    @GET("images/search")
    suspend fun getImages(
        @Query("breed_ids") breedId: String,
        @Query("limit") limit: Int = 15,
        @Query("page") page: Int,
        @Query("include_breeds") includeBreeds: Boolean = false,
        @Query("order") order: String = "ASC",
    ): List<ImageResponse>
}
