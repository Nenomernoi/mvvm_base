package org.mainsoft.base.net

import org.mainsoft.base.net.response.Breed
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("breeds")
    suspend fun getBreeds(
            @Query("limit") limit: Int,
            @Query("page") page: Int
    ): MutableList<Breed>


}