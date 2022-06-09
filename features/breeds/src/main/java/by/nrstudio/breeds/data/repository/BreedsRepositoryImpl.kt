package by.nrstudio.breeds.data.repository

import by.nrstudio.basemodulesmvi.functional_programming.Either
import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.breeds.data.data_source.BreedsRemoteDataSource
import by.nrstudio.breeds.domain.BreedsRepository
import by.nrstudio.common.models.domain.Breed
import by.nrstudio.common.models.mapper.BreedMapper

class BreedsRepositoryImpl(
    private val remoteDataSourceBreeds: BreedsRemoteDataSource,
    private val mapperBreeds: BreedMapper
) : BreedsRepository {

    override suspend fun getBreeds(page: Int, limit: Int): Either<Failure, List<Breed>> {
        return remoteDataSourceBreeds.getBreeds(page, limit)
            .coMapSuccess { items ->
                mapperBreeds.mapRemoteListToDomain(items)
            }
    }
}
