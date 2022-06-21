package org.base.breeds_data.db.repository

import org.base.db.model.BreedDb
import org.base.functional_programming.Either
import org.base.functional_programming.Failure

interface BreedsDbRepository {
    suspend fun getPageBreeds(page: Int, limit: Int): Either<Failure, List<BreedDb>>
    suspend fun saveAllBreeds(list: List<BreedDb>): Either<Failure, Boolean>
    suspend fun removeAllBreeds(): Either<Failure, Unit>
    suspend fun getBreed(breedId: String): Either<Failure, BreedDb?>
}
