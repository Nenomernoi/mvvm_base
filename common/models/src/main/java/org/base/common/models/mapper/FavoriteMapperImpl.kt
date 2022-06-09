package org.base.common.models.mapper

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
            remoteList.map {
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

    override suspend fun mapRemoteListToUi(domainList: List<Favorite>): List<FavoriteUi> {
        return withContext(defaultDispatcher) {
            domainList.map {
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
}
