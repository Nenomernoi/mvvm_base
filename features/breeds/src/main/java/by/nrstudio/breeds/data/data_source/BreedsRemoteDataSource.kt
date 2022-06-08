package by.nrstudio.breeds.data.data_source

import by.nrstudio.basemodulesmvi.functional_programming.Either
import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.common.models.data.BreedResponse

interface BreedsRemoteDataSource {
    suspend fun getBreeds(page: Int, limit: Int): Either<Failure, List<BreedResponse>>
}
