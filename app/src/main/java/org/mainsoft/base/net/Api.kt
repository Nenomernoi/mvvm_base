package org.mainsoft.base.net

import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.net.response.BreedImage
import org.mainsoft.base.net.response.Image
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("breeds")
    suspend fun getBreeds(
            @Query("limit") limit: Int,
            @Query("page") page: Int
    ): MutableList<Breed>

    @GET("images/search")
    suspend fun getBreed(
            @Query("breed_id") breedId: String
    ): MutableList<BreedImage>

    @GET("images/search")
    suspend fun getImages(
            @Query("breed_id") breedId: String,
            @Query("page") page: Int,
            @Query("limit") limit: Int,
            @Query("format") format: String
    ): MutableList<Image>

}