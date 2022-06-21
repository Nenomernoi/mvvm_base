package org.base.favorites_data.data.data_source

import org.base.common.models.data.FavoriteRequest
import org.base.common.models.domain.Favorite
import org.base.common.models.mapper.FavoriteMapper
import org.base.favorites_data.data.repository.FavoritesRemoteDataSource
import org.base.favorites_data.domain.FavoritesRepository
import org.base.functional_programming.Either
import org.base.functional_programming.Failure

class FavoritesRepositoryImpl(
    private val remoteDataSourceFavorites: FavoritesRemoteDataSource,
    private val mapperFavorites: FavoriteMapper
) : FavoritesRepository {

    override suspend fun getFavorites(page: Int, limit: Int): Either<Failure, List<Favorite>> {
        return remoteDataSourceFavorites.getFavorites(page, limit)
            .coMapSuccess { items ->
                mapperFavorites.mapRemoteListToDomain(items)
            }
    }

    override suspend fun addFavorite(request: FavoriteRequest): Either<Failure, Triple<String, Long, Boolean>> {
        return remoteDataSourceFavorites.addFavorite(request)
            .coMapSuccess {
                Triple(request.id, it.id, true)
            }
    }

    override suspend fun unFavorite(id: Long): Either<Failure, Pair<Long, Boolean>> {
        return remoteDataSourceFavorites.unFavorite(id)
            .coMapSuccess {
                Pair(id, true)
            }
    }
}
