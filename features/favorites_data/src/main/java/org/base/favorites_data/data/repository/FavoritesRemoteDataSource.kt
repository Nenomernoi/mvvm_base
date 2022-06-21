package org.base.favorites_data.data.repository

import org.base.common.models.data.FavoriteAddedSuccessResponse
import org.base.common.models.data.FavoriteRemoveSuccessResponse
import org.base.common.models.data.FavoriteRequest
import org.base.common.models.data.FavoriteResponse
import org.base.functional_programming.Either
import org.base.functional_programming.Failure

interface FavoritesRemoteDataSource {
    suspend fun getFavorites(page: Int, limit: Int): Either<Failure, List<FavoriteResponse>>

    suspend fun addFavorite(request: FavoriteRequest): Either<Failure, FavoriteAddedSuccessResponse>

    suspend fun unFavorite(id: Long): Either<Failure, FavoriteRemoveSuccessResponse>
}
