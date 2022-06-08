package by.nrstudio.common.models.mapper

import by.nrstudio.common.models.data.BreedResponse
import by.nrstudio.common.models.domain.Breed
import by.nrstudio.common.models.presentation.BreedUi

interface BreedMapper {
    suspend fun mapRemoteListToDomain(remoteList: List<BreedResponse>): List<Breed>
    suspend fun mapRemoteToDomain(remote: BreedResponse): Breed
    suspend fun mapRemoteListToUi(domainList: List<Breed>): List<BreedUi>
    suspend fun mapRemoteToUi(domain: Breed): BreedUi
}
