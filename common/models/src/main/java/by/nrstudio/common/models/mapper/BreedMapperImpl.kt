package by.nrstudio.common.models.mapper

import by.nrstudio.common.models.data.BreedResponse
import by.nrstudio.common.models.domain.Breed
import by.nrstudio.common.models.presentation.BreedUi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

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
