package org.base.common.models.mapper

import org.base.db.model.FavoriteDb
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.base.common.models.data.FavoriteResponse
import org.base.common.models.domain.Favorite
import org.base.common.models.presentation.FavoriteUi

class FavoriteMapperImpl(
    private val defaultDispatcher: CoroutineDispatcher
) : FavoriteMapper {

    override suspend fun mapRemoteListToDomain(remoteList: List<FavoriteResponse>): List<Favorite> {
        return withContext(defaultDispatcher) {
            remoteList.sortedBy { it.id }.map {
                mapRemoteToDomain(it)
            }
        }
    }

    override suspend fun mapRemoteToDomain(remote: FavoriteResponse) = Favorite(
        id = remote.id,
        imageId = remote.imageId,
        image = remote.image.url,

        isFavorite = true
    )

    override suspend fun mapDomainListToDb(domainList: List<Favorite>): List<FavoriteDb> {
        return withContext(defaultDispatcher) {
            domainList.sortedBy { it.id }.map {
                mapDomainToDb(it)
            }
        }
    }

    override suspend fun mapDomainToDb(domain: Favorite) = FavoriteDb(
        id = domain.id,
        imageId = domain.imageId,
        image = domain.image,

        isFavorite = domain.isFavorite
    )

    override suspend fun mapRemoteListToUi(domainList: List<Favorite>): List<FavoriteUi> {
        return withContext(defaultDispatcher) {
            domainList.sortedBy { it.id }.map {
                mapRemoteToUi(it)
            }
        }
    }

    override suspend fun mapRemoteToUi(domain: Favorite) = FavoriteUi(
        id = domain.id,
        imageId = domain.imageId,
        image = domain.image,

        isFavorite = domain.isFavorite
    )

    override suspend fun mapDbListToUi(dbList: List<FavoriteDb>): List<FavoriteUi> {
        return withContext(defaultDispatcher) {
            dbList.map {
                mapDbToUi(it)
            }
        }
    }

    override suspend fun mapDbToUi(db: FavoriteDb) = FavoriteUi(
        id = db.id,
        imageId = db.imageId,
        image = db.image,

        isFavorite = db.isFavorite
    )
}
