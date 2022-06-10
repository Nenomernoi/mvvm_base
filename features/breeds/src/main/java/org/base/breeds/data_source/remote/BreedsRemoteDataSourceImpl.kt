package org.base.breeds.data_source.remote

import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher
import org.base.breeds.data.repository.BreedsRemoteDataSource
import org.base.breeds.data_source.remote.retrofit_service.BreedsService
import org.base.common.models.data.BreedResponse
import org.base.main.functional_programming.Either
import org.base.main.functional_programming.Failure
import org.base.network.middleware.provider.MiddlewareProvider
import org.base.network.models.base.ResponseError
import org.base.network.utils.call

internal class BreedsRemoteDataSourceImpl(
    private val middlewareProvider: MiddlewareProvider,
    private val ioDispatcher: CoroutineDispatcher,
    private val adapter: JsonAdapter<ResponseError>,
    private val serviceBreeds: BreedsService
) : BreedsRemoteDataSource {

    override suspend fun getBreeds(page: Int, limit: Int): Either<Failure, List<BreedResponse>> {
        return call(
            middleWares = middlewareProvider.getAll(),
            ioDispatcher = ioDispatcher,
            adapter = adapter,
            retrofitCall = {
                serviceBreeds.getBreeds(
                    limit = limit,
                    page = page
                )
            }
        ).let { response -> response.mapSuccess { responseItems -> responseItems } }
    }
}
