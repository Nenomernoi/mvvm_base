package by.nrstudio.favorites.presentation.ui.favorites.processor

import by.nrstudio.basemodulesmvi.functional_programming.Either
import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.common.models.data.FavoriteRequest
import by.nrstudio.common.models.mapper.FavoriteMapper
import by.nrstudio.common.models.presentation.FavoriteUi
import by.nrstudio.favorites.domain.FavoritesRepository
import by.nrstudio.favorites.presentation.ui.favorites.action.FavoritesAction
import by.nrstudio.favorites.presentation.ui.favorites.result.FavoritesResult
import by.nrstudio.mvi.MviProcessorHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesProcessorHolder(
    private val repositoryFavorites: FavoritesRepository,
    private val mapperFavorites: FavoriteMapper
) : MviProcessorHolder<FavoritesAction, FavoritesResult> {

    override fun processAction(action: FavoritesAction, vararg values: Any): Flow<FavoritesResult> {
        return flow {
            when (action) {
                FavoritesAction.LoadFavoritesAction, FavoritesAction.ReLoadLastFavoritesAction, FavoritesAction.LoadNextFavoritesAction -> {
                    if (action != FavoritesAction.LoadNextFavoritesAction) emit(FavoritesResult.Loading)
                    val response: Either<Failure, List<FavoriteUi>> =
                        repositoryFavorites.getFavorites(page = values.first() as Int, limit = values[1] as Int)
                            .coMapSuccess { domain ->
                                mapperFavorites.mapRemoteListToUi(domain)
                            }
                    val result = handleSuccessOrFailure(result = response)
                    emit(result)
                }
                FavoritesAction.AddItemAction -> {
                    val idItem = values.first().toString()
                    emit(FavoritesResult.Loading)
                    val response: Either<Failure, Triple<String, Long, Boolean>> =
                        repositoryFavorites.addFavorite(FavoriteRequest(id = idItem))
                    val result = handleSuccessOrFailureAdd(result = response)
                    emit(result)
                }
                FavoritesAction.RemoveItemAction -> {
                    emit(FavoritesResult.Loading)
                    val response: Either<Failure, Pair<Long, Boolean>> =
                        repositoryFavorites.unFavorite(id = values.first() as Long)
                    val result = handleSuccessOrFailureRemove(result = response)
                    emit(result)
                }
            }
        }
    }

    private fun handleSuccessOrFailure(result: Either<Failure, List<FavoriteUi>>): FavoritesResult {
        return when (result) {
            is Either.Error -> FavoritesResult.Error(failure = result.error)
            is Either.Success -> FavoritesResult.Success(items = result.success)
        }
    }

    private fun handleSuccessOrFailureAdd(result: Either<Failure, Triple<String, Long, Boolean>>): FavoritesResult {
        return when (result) {
            is Either.Error -> FavoritesResult.Error(failure = result.error)
            is Either.Success -> FavoritesResult.SuccessAdd(
                id = result.success.first,
                idNew = result.success.second,
                status = result.success.third
            )
        }
    }

    private fun handleSuccessOrFailureRemove(result: Either<Failure, Pair<Long, Boolean>>): FavoritesResult {
        return when (result) {
            is Either.Error -> FavoritesResult.Error(failure = result.error)
            is Either.Success -> FavoritesResult.SuccessRemove(id = result.success.first, status = result.success.second)
        }
    }
}
