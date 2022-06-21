package org.base.breed_data.data.data_source

import org.base.breed_data.data.repository.ImagesRemoteDataSource
import org.base.breed_data.domain.ImagesRepository
import org.base.common.models.domain.Image
import org.base.common.models.mapper.ImageMapper
import org.base.functional_programming.Either
import org.base.functional_programming.Failure

class ImagesRepositoryImpl(
    private val remoteDataSourceImage: ImagesRemoteDataSource,
    private val mapperImages: ImageMapper,
) : ImagesRepository {

    override suspend fun getImages(breedId: String, page: Int, limit: Int): Either<Failure, List<Image>> {
        return remoteDataSourceImage.getImages(breedId, page, limit)
            .coMapSuccess { items ->
                mapperImages.mapRemoteListToDomain(items)
            }
    }
}
