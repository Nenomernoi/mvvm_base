package org.base.favorites_data.data_source.remote

import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher
import org.base.common.models.data.FavoriteAddedSuccessResponse
import org.base.common.models.data.FavoriteRemoveSuccessResponse
import org.base.common.models.data.FavoriteRequest
import org.base.common.models.data.FavoriteResponse
import org.base.favorites_data.data.repository.FavoritesRemoteDataSource
import org.base.favorites_data.data_source.remote.retrofit_service.FavoritesService
import org.base.functional_programming.Either
import org.base.functional_programming.Failure
import org.base.network.middleware.provider.MiddlewareProvider
import org.base.network.models.base.ResponseError
import org.base.network.utils.call

class FavoritesRemoteDataSourceImpl(
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
