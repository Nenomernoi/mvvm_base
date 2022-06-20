package org.base.breed_data.data_source.remote

import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher
import org.base.breed_data.data.repository.ImagesRemoteDataSource
import org.base.breed_data.data_source.remote.retrofit_service.ImagesService
import org.base.common.models.data.ImageResponse
import org.base.main.functional_programming.Either
import org.base.main.functional_programming.Failure
import org.base.network.middleware.provider.MiddlewareProvider
import org.base.network.models.base.ResponseError
import org.base.network.utils.call

class ImagesRemoteDataSourceImpl(
    private val middlewareProvider: MiddlewareProvider,
    private val ioDispatcher: CoroutineDispatcher,
    private val adapter: JsonAdapter<ResponseError>,
    private val serviceImages: ImagesService
) : ImagesRemoteDataSource {

    override suspend fun getImages(breedId: String, page: Int, limit: Int): Either<Failure, List<ImageResponse>> {
        return call(
            middleWares = middlewareProvider.getAll(),
            ioDispatcher = ioDispatcher,
            adapter = adapter,
            retrofitCall = {
                serviceImages.getImages(
                    breedId = breedId,
                    limit = limit,
                    page = page
                )
            }
        ).let { response -> response.mapSuccess { responseItems -> responseItems } }
    }
}
