package by.nrstudio.favorites.domain

import by.nrstudio.basemodulesmvi.functional_programming.Either
import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.common.models.data.FavoriteRequest
import by.nrstudio.common.models.domain.Favorite

interface FavoritesRepository {

    suspend fun getFavorites(page: Int, limit: Int): Either<Failure, List<Favorite>>

    suspend fun addFavorite(request: FavoriteRequest): Either<Failure, Triple<String, Long, Boolean>>

    suspend fun unFavorite(id: Long): Either<Failure, Pair<Long, Boolean>>
}
