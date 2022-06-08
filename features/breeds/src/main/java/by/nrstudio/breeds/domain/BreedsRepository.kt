package by.nrstudio.breeds.domain

import by.nrstudio.basemodulesmvi.functional_programming.Either
import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.common.models.domain.Breed

interface BreedsRepository {
    suspend fun getBreeds(page: Int, limit: Int): Either<Failure, List<Breed>>
}
