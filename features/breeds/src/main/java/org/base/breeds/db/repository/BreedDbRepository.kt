package org.base.breeds.db.repository

import org.base.db.model.BreedDb
import org.base.main.functional_programming.Either
import org.base.main.functional_programming.Failure

interface BreedDbRepository {
    suspend fun getPageBreeds(page: Int, limit: Int): Either<Failure, List<BreedDb>>
    suspend fun saveAllBreeds(list: List<BreedDb>): Either<Failure, Boolean>
    suspend fun removeAllBreeds(): Either<Failure, Unit>
}
