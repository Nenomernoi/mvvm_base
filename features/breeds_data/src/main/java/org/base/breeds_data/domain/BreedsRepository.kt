package org.base.breeds_data.domain

import org.base.common.models.domain.Breed
import org.base.functional_programming.Either
import org.base.functional_programming.Failure

interface BreedsRepository {
    suspend fun getBreeds(page: Int, limit: Int): Either<Failure, List<Breed>>
}
