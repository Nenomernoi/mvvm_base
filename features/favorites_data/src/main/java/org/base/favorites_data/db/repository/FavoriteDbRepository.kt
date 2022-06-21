package org.base.favorites_data.db.repository

import org.base.db.model.FavoriteDb
import org.base.functional_programming.Either
import org.base.functional_programming.Failure

interface FavoriteDbRepository {
    suspend fun getPageFavorites(page: Int, limit: Int): Either<Failure, List<FavoriteDb>>
    suspend fun saveAllFavorites(list: List<FavoriteDb>): Either<Failure, Boolean>
    suspend fun saveFavorite(item: FavoriteDb): Either<Failure, Boolean>
    suspend fun removeAllFavorites(): Either<Failure, Unit>
    suspend fun removeUnFavorites(): Either<Failure, Unit>
    suspend fun addToFavorites(imageId: String): Either<Failure, Unit>
    suspend fun removeFromFavorites(id: Long): Either<Failure, Unit>
}
