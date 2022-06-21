package org.base.breed_data.data.repository

import org.base.common.models.data.ImageResponse
import org.base.functional_programming.Either
import org.base.functional_programming.Failure

interface ImagesRemoteDataSource {
    suspend fun getImages(breedId: String, page: Int, limit: Int): Either<Failure, List<ImageResponse>>
}
