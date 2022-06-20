package org.base.favorites_data.domain

import org.base.common.models.data.FavoriteRequest
import org.base.common.models.domain.Favorite
import org.base.main.functional_programming.Either
import org.base.main.functional_programming.Failure

interface FavoritesRepository {

    suspend fun getFavorites(page: Int, limit: Int): Either<Failure, List<Favorite>>

    suspend fun addFavorite(request: FavoriteRequest): Either<Failure, Triple<String, Long, Boolean>>

    suspend fun unFavorite(id: Long): Either<Failure, Pair<Long, Boolean>>
}
