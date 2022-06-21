package org.base.breed_data.db.repository

import org.base.db.model.ImageDb
import org.base.functional_programming.Either
import org.base.functional_programming.Failure

interface ImageDbRepository {
    suspend fun getPageImages(breedId: String, page: Int, limit: Int): Either<Failure, List<ImageDb>>
    suspend fun saveAllImages(list: List<ImageDb>): Either<Failure, Boolean>
    suspend fun saveImage(item: ImageDb): Either<Failure, Boolean>
}
