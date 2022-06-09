package org.base.common.models.mapper

import org.base.common.models.data.FavoriteResponse
import org.base.common.models.domain.Favorite
import org.base.common.models.presentation.FavoriteUi

interface FavoriteMapper {
    suspend fun mapRemoteListToDomain(remoteList: List<FavoriteResponse>): List<Favorite>
    suspend fun mapRemoteToDomain(remote: FavoriteResponse): Favorite
    suspend fun mapRemoteListToUi(domainList: List<Favorite>): List<FavoriteUi>
    suspend fun mapRemoteToUi(domain: Favorite): FavoriteUi
}
