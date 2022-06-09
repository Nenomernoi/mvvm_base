package by.nrstudio.favorites.data_source.remote

import by.nrstudio.basemodulesmvi.functional_programming.Either
import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.common.models.data.FavoriteAddedSuccessResponse
import by.nrstudio.common.models.data.FavoriteRemoveSuccessResponse
import by.nrstudio.common.models.data.FavoriteRequest
import by.nrstudio.common.models.data.FavoriteResponse
import by.nrstudio.favorites.data.data_source.FavoritesRemoteDataSource
import by.nrstudio.favorites.data_source.remote.retrofit_service.FavoritesService
import by.nrstudio.network.middleware.provider.MiddlewareProvider
import by.nrstudio.network.models.base.ResponseError
import by.nrstudio.network.utils.call
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher

internal class FavoritesRemoteDtaSourceImpl(
    private val middlewareProvider: MiddlewareProvider,
    private val ioDispatcher: CoroutineDispatcher,
    private val adapter: JsonAdapter<ResponseError>,
    private val serviceFavorites: FavoritesService
) : FavoritesRemoteDataSource {

    override suspend fun getFavorites(page: Int, limit: Int): Either<Failure, List<FavoriteResponse>> {
        return call(
            middleWares = middlewareProvider.getAll(),
            ioDispatcher = ioDispatcher,
            adapter = adapter,
            retrofitCall = {
                serviceFavorites.getFavorites(
                    limit = limit,
                    page = page
                )
            }
        ).let { response -> response.mapSuccess { responseItems -> responseItems } }
    }

    override suspend fun addFavorite(request: FavoriteRequest): Either<Failure, FavoriteAddedSuccessResponse> {
        return call(
            middleWares = middlewareProvider.getAll(),
            ioDispatcher = ioDispatcher,
            adapter = adapter,
            retrofitCall = {
                serviceFavorites.addFavorite(
                    request = request
                )
            }
        ).let { response -> response.mapSuccess { responseItem -> responseItem } }
    }

    override suspend fun unFavorite(id: Long): Either<Failure, FavoriteRemoveSuccessResponse> {
        return call(
            middleWares = middlewareProvider.getAll(),
            ioDispatcher = ioDispatcher,
            adapter = adapter,
            retrofitCall = {
                serviceFavorites.unFavorite(
                    id = id
                )
            }
        ).let { response -> response.mapSuccess { responseItem -> responseItem } }
    }
}
