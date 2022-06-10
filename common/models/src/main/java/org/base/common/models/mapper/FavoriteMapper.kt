package org.base.common.models.mapper

import org.base.db.model.FavoriteDb
import org.base.common.models.data.FavoriteResponse
import org.base.common.models.domain.Favorite
import org.base.common.models.presentation.FavoriteUi

interface FavoriteMapper {
    suspend fun mapRemoteListToDomain(remoteList: List<FavoriteResponse>): List<Favorite>
    suspend fun mapRemoteToDomain(remote: FavoriteResponse): Favorite

    suspend fun mapDomainListToDb(domainList: List<Favorite>): List<FavoriteDb>
    suspend fun mapDomainToDb(domain: Favorite): FavoriteDb

    suspend fun mapRemoteListToUi(domainList: List<Favorite>): List<FavoriteUi>
    suspend fun mapRemoteToUi(domain: Favorite): FavoriteUi

    suspend fun mapDbListToUi(dbList: List<FavoriteDb>): List<FavoriteUi>
    suspend fun mapDbToUi(db: FavoriteDb): FavoriteUi
}
