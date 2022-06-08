package by.nrstudio.favorites.data_source.remote

import by.nrstudio.common.models.data.FavoriteAddedSuccessResponse
import by.nrstudio.common.models.data.FavoriteRemoveSuccessResponse
import by.nrstudio.common.models.data.FavoriteRequest
import by.nrstudio.common.models.data.FavoriteResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface FavoritesService {

    @GET("favourites")
    suspend fun getFavorites(
        @Query("limit") limit: Int = 15,
        @Query("page") page: Int
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
