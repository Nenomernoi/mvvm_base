package org.base.common.models.mapper

import org.base.common.models.data.BreedResponse
import org.base.common.models.domain.Breed
import org.base.common.models.presentation.BreedUi
import org.base.db.model.BreedDb

interface BreedMapper {
    suspend fun mapRemoteListToDomain(remoteList: List<BreedResponse>): List<Breed>
    suspend fun mapRemoteToDomain(remote: BreedResponse): Breed

    suspend fun mapDomainListToDb(domainList: List<Breed>): List<BreedDb>
    suspend fun mapDomainToDb(domain: Breed): BreedDb

    suspend fun mapRemoteListToUi(domainList: List<Breed>): List<BreedUi>
    suspend fun mapRemoteToUi(domain: Breed): BreedUi

    suspend fun mapDbListToUi(dbList: List<BreedDb>): List<BreedUi>
    suspend fun mapDbToUi(db: BreedDb): BreedUi
}
