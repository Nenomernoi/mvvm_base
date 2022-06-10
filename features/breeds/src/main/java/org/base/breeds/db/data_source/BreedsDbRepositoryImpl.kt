package org.base.breeds.db.data_source

import kotlinx.coroutines.CoroutineDispatcher
import org.base.breeds.db.repository.BreedsDbRepository
import org.base.db.call
import org.base.db.dao.BreedDao
import org.base.db.model.BreedDb
import org.base.main.functional_programming.Either
import org.base.main.functional_programming.Failure

class BreedsDbRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val daoBreeds: BreedDao
) : BreedsDbRepository {

    override suspend fun getPageBreeds(page: Int, limit: Int): Either<Failure, List<BreedDb>> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                daoBreeds.getPage(page * limit, limit)
            }
        ).let { response -> response.mapSuccess { responseItems -> responseItems } }
    }

    override suspend fun saveAllBreeds(list: List<BreedDb>): Either<Failure, Boolean> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                daoBreeds.insert(list).isNotEmpty()
            }
        ).let { response -> response.mapSuccess { responseItems -> responseItems } }
    }

    override suspend fun removeAllBreeds(): Either<Failure, Unit> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                daoBreeds.deleteAll()
            }
        )
    }
}
