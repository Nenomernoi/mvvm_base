package org.base.breed_data.domain

import org.base.common.models.domain.Image
import org.base.main.functional_programming.Either
import org.base.main.functional_programming.Failure

interface ImagesRepository {
    suspend fun getImages(breedId: String, page: Int, limit: Int): Either<Failure, List<Image>>
}
