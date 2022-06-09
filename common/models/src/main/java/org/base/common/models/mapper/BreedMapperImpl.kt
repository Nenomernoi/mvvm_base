package org.base.common.models.mapper

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.base.common.models.data.BreedResponse
import org.base.common.models.domain.Breed
import org.base.common.models.presentation.BreedUi

class BreedMapperImpl(
    private val defaultDispatcher: CoroutineDispatcher
) : BreedMapper {

    override suspend fun mapRemoteListToDomain(remoteList: List<BreedResponse>): List<Breed> {
        return withContext(defaultDispatcher) {
            remoteList.map {
                mapRemoteToDomain(it)
            }
        }
    }

    override suspend fun mapRemoteToDomain(remote: BreedResponse) =
        Breed(
            id = remote.id,
            name = remote.name,
            description = remote.description,
            image = remote.image?.url ?: "",
            weight = remote.weight.metric,
            lifeSpan = remote.lifeSpan,
            countryCode = remote.countryCode,
            origin = remote.origin,
        )

    override suspend fun mapRemoteListToUi(domainList: List<Breed>): List<BreedUi> {
        return withContext(defaultDispatcher) {
            domainList.map {
                mapRemoteToUi(it)
            }
        }
    }

    override suspend fun mapRemoteToUi(domain: Breed) =
        BreedUi(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            image = domain.image,
            countryFlag = "https://countryflagsapi.com/png/${domain.countryCode.lowercase()}",
        )
}
