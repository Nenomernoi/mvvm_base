package org.base.breeds.data.repository

import org.base.breeds.data.data_source.BreedsRemoteDataSource
import org.base.breeds.domain.BreedsRepository
import org.base.common.models.domain.Breed
import org.base.common.models.mapper.BreedMapper
import org.base.main.functional_programming.Either
import org.base.main.functional_programming.Failure

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
