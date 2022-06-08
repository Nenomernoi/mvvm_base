package by.nrstudio.breeds.data_source.remote.retrofit_service

import by.nrstudio.common.models.data.BreedResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface BreedsService {

    @GET("breeds")
    suspend fun getBreeds(
        @Query("limit") limit: Int = 15,
        @Query("page") page: Int
    ): List<BreedResponse>
}
