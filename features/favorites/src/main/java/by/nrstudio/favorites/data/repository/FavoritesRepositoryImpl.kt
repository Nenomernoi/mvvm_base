package by.nrstudio.favorites.data.repository

import by.nrstudio.basemodulesmvi.functional_programming.Either
import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.common.models.data.FavoriteRequest
import by.nrstudio.common.models.domain.Favorite
import by.nrstudio.common.models.mapper.FavoriteMapper
import by.nrstudio.favorites.data.data_source.FavoritesRemoteDataSource
import by.nrstudio.favorites.domain.FavoritesRepository

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
