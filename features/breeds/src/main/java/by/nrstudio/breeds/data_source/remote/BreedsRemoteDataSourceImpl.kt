package by.nrstudio.breeds.data_source.remote

import by.nrstudio.basemodulesmvi.functional_programming.Either
import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.breeds.data.data_source.BreedsRemoteDataSource
import by.nrstudio.breeds.data_source.remote.retrofit_service.BreedsService
import by.nrstudio.common.models.data.BreedResponse
import by.nrstudio.network.middleware.provider.MiddlewareProvider
import by.nrstudio.network.models.base.ResponseError
import by.nrstudio.network.utils.call
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher

internal class BreedsRemoteDataSourceImpl(
    private val middlewareProvider: MiddlewareProvider,
    private val ioDispatcher: CoroutineDispatcher,
    private val adapter: JsonAdapter<ResponseError>,
    private val movieService: BreedsService
) : BreedsRemoteDataSource {

    override suspend fun getBreeds(page: Int, limit: Int): Either<Failure, List<BreedResponse>> {
        return call(
            middleWares = middlewareProvider.getAll(),
            ioDispatcher = ioDispatcher,
            adapter = adapter,
            retrofitCall = {
                movieService.getBreeds(
                    limit = limit,
                    page = page
                )
            }
        ).let { response -> response.mapSuccess { responseItems -> responseItems } }
    }
}
