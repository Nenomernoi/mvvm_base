package org.base.common.models.mapper

import org.base.common.models.data.ImageResponse
import org.base.common.models.domain.Image
import org.base.common.models.presentation.ImageUi
import org.base.db.model.ImageDb

interface ImageMapper {
    suspend fun mapRemoteListToDomain(remoteList: List<ImageResponse>): List<Image>
    suspend fun mapRemoteToDomain(remote: ImageResponse): Image

    suspend fun mapDomainListToDb(domainList: List<Image>, breedId: String): List<ImageDb>
    suspend fun mapDomainToDb(domain: Image, breedId: String): ImageDb

    suspend fun mapRemoteListToUi(domainList: List<Image>): List<ImageUi>
    suspend fun mapRemoteToUi(domain: Image): ImageUi

    suspend fun mapDbListToUi(dbList: List<ImageDb>): List<ImageUi>
    suspend fun mapDbToUi(db: ImageDb): ImageUi

    suspend fun mapUiToDb(ui: ImageUi, breedId: String): ImageDb
}
