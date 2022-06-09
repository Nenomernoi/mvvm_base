package by.nrstudio.favorites.data.data_source

import by.nrstudio.basemodulesmvi.functional_programming.Either
import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.common.models.data.FavoriteAddedSuccessResponse
import by.nrstudio.common.models.data.FavoriteRemoveSuccessResponse
import by.nrstudio.common.models.data.FavoriteRequest
import by.nrstudio.common.models.data.FavoriteResponse

interface FavoritesRemoteDataSource {
    suspend fun getFavorites(page: Int, limit: Int): Either<Failure, List<FavoriteResponse>>

    suspend fun addFavorite(request: FavoriteRequest): Either<Failure, FavoriteAddedSuccessResponse>

    suspend fun unFavorite(id: Long): Either<Failure, FavoriteRemoveSuccessResponse>
}
