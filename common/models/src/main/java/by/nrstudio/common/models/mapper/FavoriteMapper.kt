package by.nrstudio.common.models.mapper

import by.nrstudio.common.models.data.FavoriteResponse
import by.nrstudio.common.models.domain.Favorite
import by.nrstudio.common.models.presentation.FavoriteUi

interface FavoriteMapper {
    suspend fun mapRemoteListToDomain(remoteList: List<FavoriteResponse>): List<Favorite>
    suspend fun mapRemoteToDomain(remote: FavoriteResponse): Favorite
    suspend fun mapRemoteListToUi(domainList: List<Favorite>): List<FavoriteUi>
    suspend fun mapRemoteToUi(domain: Favorite): FavoriteUi
}
