package org.base.breed_data.db.data_source

import kotlinx.coroutines.CoroutineDispatcher
import org.base.breed_data.db.repository.ImageDbRepository
import org.base.db.call
import org.base.db.dao.ImageDao
import org.base.db.model.ImageDb
import org.base.functional_programming.Either
import org.base.functional_programming.Failure

class ImageDbRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val daoImages: ImageDao
) : ImageDbRepository {

    override suspend fun getPageImages(breedId: String, page: Int, limit: Int): Either<Failure, List<ImageDb>> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                daoImages.getPage(breedId, page * limit, limit)
            }
        ).let { response -> response.mapSuccess { responseItems -> responseItems } }
    }

    override suspend fun saveAllImages(list: List<ImageDb>): Either<Failure, Boolean> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                daoImages.insert(list).isNotEmpty()
            }
        ).let { response -> response.mapSuccess { responseItems -> responseItems } }
    }

    override suspend fun saveImage(item: ImageDb): Either<Failure, Boolean> {
        return call(
            ioDispatcher = ioDispatcher,
            dbCall = {
                daoImages.insert(item) > 0L
            }
        ).let { response -> response.mapSuccess { responseItems -> responseItems } }
    }
}
