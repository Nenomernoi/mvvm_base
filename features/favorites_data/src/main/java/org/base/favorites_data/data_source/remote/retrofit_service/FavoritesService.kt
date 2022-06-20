package org.base.favorites_data.data_source.remote.retrofit_service

import org.base.common.models.data.FavoriteAddedSuccessResponse
import org.base.common.models.data.FavoriteRemoveSuccessResponse
import org.base.common.models.data.FavoriteRequest
import org.base.common.models.data.FavoriteResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FavoritesService {

    @GET("favourites")
    suspend fun getFavorites(
        @Query("limit") limit: Int = 15,
        @Query("page") page: Int,
        @Query("include_breeds") includeBreeds: Boolean = false,
    ): List<FavoriteResponse>

    @POST("favourites")
    suspend fun addFavorite(
        @Body request: FavoriteRequest,
    ): FavoriteAddedSuccessResponse

    @DELETE("favourites/{id}")
    suspend fun unFavorite(
        @Path("id") id: Long,
    ): FavoriteRemoveSuccessResponse
}
