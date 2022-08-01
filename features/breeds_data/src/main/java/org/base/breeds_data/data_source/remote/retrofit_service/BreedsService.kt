package org.base.breeds_data.data_source.remote.retrofit_service

import org.base.common.models.data.BreedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BreedsService {

    @GET("v1/breeds")
    suspend fun getBreeds(
        @Query("limit") limit: Int = 15,
        @Query("page") page: Int
    ): List<BreedResponse>
}
