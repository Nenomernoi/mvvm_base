package org.base.favorites_data.db.data_source

import kotlinx.coroutines.CoroutineDispatcher
import org.base.db.call
import org.base.db.dao.FavoriteDao
import org.base.db.model.FavoriteDb
import org.base.favorites_data.db.repository.FavoriteDbRepository
import org.base.main.functional_programming.Either
import org.base.main.functional_programming.Failure

class FavoriteDbRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val daoFavorites: FavoriteDao
) : FavoriteDbRepository {

    override suspend fun getPageFavorites(page: Int, limit: Int): Either<Failure, List<FavoriteDb>> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                daoFavorites.getPage(page * limit, limit)
            }
        ).let { response -> response.mapSuccess { responseItems -> responseItems } }
    }

    override suspend fun saveAllFavorites(list: List<FavoriteDb>): Either<Failure, Boolean> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                daoFavorites.insert(list).isNotEmpty()
            }
        ).let { response -> response.mapSuccess { responseItems -> responseItems } }
    }

    override suspend fun saveFavorite(item: FavoriteDb): Either<Failure, Boolean> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                daoFavorites.insert(item) > 0L
            }
        ).let { response -> response.mapSuccess { responseItems -> responseItems } }
    }

    override suspend fun removeAllFavorites(): Either<Failure, Unit> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                daoFavorites.deleteAll()
            }
        )
    }

    override suspend fun removeUnFavorites(): Either<Failure, Unit> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                daoFavorites.deleteUnFavoriteAll()
            }
        )
    }

    override suspend fun addToFavorites(imageId: String): Either<Failure, Unit> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                val item = daoFavorites.getItem(imageId = imageId)?.apply {
                    this.isFavorite = true
                }
                daoFavorites.update(item ?: return@call)
            }
        )
    }

    override suspend fun removeFromFavorites(id: Long): Either<Failure, Unit> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                val item = daoFavorites.getItem(uuid = id)?.apply {
                    this.isFavorite = false
                }
                daoFavorites.update(item ?: return@call)
            }
        )
    }
}
