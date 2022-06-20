package org.base.common.models.mapper

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.base.common.models.data.ImageResponse
import org.base.common.models.domain.Image
import org.base.common.models.presentation.ImageUi
import org.base.db.model.ImageDb

class ImageMapperImpl(
    private val defaultDispatcher: CoroutineDispatcher
) : ImageMapper {

    override suspend fun mapRemoteListToDomain(remoteList: List<ImageResponse>): List<Image> {
        return withContext(defaultDispatcher) {
            remoteList.sortedBy { it.id }.map {
                mapRemoteToDomain(it)
            }
        }
    }

    override suspend fun mapRemoteToDomain(remote: ImageResponse) = Image(
        id = remote.id,
        url = remote.url,
    )

    override suspend fun mapDomainListToDb(domainList: List<Image>, breedId: String): List<ImageDb> {
        return withContext(defaultDispatcher) {
            domainList.sortedBy { it.id }.map {
                mapDomainToDb(it, breedId)
            }
        }
    }

    override suspend fun mapDomainToDb(domain: Image, breedId: String) = ImageDb(
        uuid = domain.id,
        idBreed = breedId,
        url = domain.url,
    )

    override suspend fun mapRemoteListToUi(domainList: List<Image>, map: Map<String, Long>): List<ImageUi> {
        return withContext(defaultDispatcher) {
            domainList.sortedBy { it.id }.map {
                mapRemoteToUi(it, map.getOrElse(it.id) { 0L })
            }
        }
    }

    override suspend fun mapRemoteToUi(domain: Image, idFavorite: Long) = ImageUi(
        id = domain.id,
        url = domain.url,
        idFavorite = idFavorite
    )

    override suspend fun mapDbListToUi(dbList: List<ImageDb>): List<ImageUi> {
        return withContext(defaultDispatcher) {
            dbList.sortedBy { it.uuid }.map {
                mapDbToUi(it)
            }
        }
    }

    override suspend fun mapDbToUi(db: ImageDb) = ImageUi(
        id = db.uuid,
        url = db.url,
        idFavorite = db.idFavorite
    )

    override suspend fun mapUiToDb(ui: ImageUi, breedId: String) = ImageDb(
        uuid = ui.id,
        idBreed = breedId,
        url = ui.url,
        idFavorite = ui.idFavorite
    )
}
