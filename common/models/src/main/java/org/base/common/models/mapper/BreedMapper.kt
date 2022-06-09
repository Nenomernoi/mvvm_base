package org.base.common.models.mapper

import org.base.common.models.data.BreedResponse
import org.base.common.models.domain.Breed
import org.base.common.models.presentation.BreedUi

interface BreedMapper {
    suspend fun mapRemoteListToDomain(remoteList: List<BreedResponse>): List<Breed>
    suspend fun mapRemoteToDomain(remote: BreedResponse): Breed
    suspend fun mapRemoteListToUi(domainList: List<Breed>): List<BreedUi>
    suspend fun mapRemoteToUi(domain: Breed): BreedUi
}
