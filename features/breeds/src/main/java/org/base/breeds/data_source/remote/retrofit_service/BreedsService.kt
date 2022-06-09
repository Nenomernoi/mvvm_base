package org.base.breeds.data_source.remote.retrofit_service

import org.base.common.models.data.BreedResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface BreedsService {

    @GET("breeds")
    suspend fun getBreeds(
        @Query("limit") limit: Int = 15,
        @Query("page") page: Int
    ): List<BreedResponse>
}
