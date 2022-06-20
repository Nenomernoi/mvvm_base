package org.base.breeds_data.data.repository

import org.base.common.models.data.BreedResponse
import org.base.main.functional_programming.Either
import org.base.main.functional_programming.Failure

interface BreedsRemoteDataSource {
    suspend fun getBreeds(page: Int, limit: Int): Either<Failure, List<BreedResponse>>
}