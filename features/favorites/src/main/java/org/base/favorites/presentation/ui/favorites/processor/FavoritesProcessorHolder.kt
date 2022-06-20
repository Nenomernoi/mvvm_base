package org.base.favorites.presentation.ui.favorites.processor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.base.common.models.data.FavoriteRequest
import org.base.common.models.domain.Favorite
import org.base.common.models.mapper.FavoriteMapper
import org.base.common.models.presentation.FavoriteUi
import org.base.favorites_data.db.repository.FavoriteDbRepository
import org.base.favorites_data.domain.FavoritesRepository
import org.base.favorites.presentation.ui.favorites.action.FavoritesAction
import org.base.favorites.presentation.ui.favorites.result.FavoritesResult
import org.base.main.functional_programming.Either
import org.base.main.functional_programming.Failure
import org.base.mvi.MviProcessorHolder

class FavoritesProcessorHolder(
    private val repositoryFavorites: FavoritesRepository,
    private val repositoryDbFavorites: FavoriteDbRepository,
    private val mapperFavorites: FavoriteMapper
) : MviProcessorHolder<FavoritesAction, FavoritesResult> {

    override fun processAction(action: FavoritesAction, vararg values: Any): Flow<FavoritesResult> {
        return flow {
            when (action) {
                FavoritesAction.LoadFavoritesAction,
                FavoritesAction.ReLoadLastFavoritesAction,
                FavoritesAction.ReLoadFavoritesAction,
                FavoritesAction.LoadNextFavoritesAction -> {
                    if (action != FavoritesAction.LoadNextFavoritesAction) {
                        emit(FavoritesResult.Loading)
                    }

                    val replaceOrAdd = if (action == FavoritesAction.ReLoadFavoritesAction) {
                        repositoryDbFavorites.removeAllFavorites()
                        false
                    } else {

                        val responseDb: Either<Failure, List<FavoriteUi>> =
                            repositoryDbFavorites.getPageFavorites(page = values.first() as Int, limit = values[1] as Int)
                                .coMapSuccess { db ->
                                    mapperFavorites.mapDbListToUi(db)
                                }
                        if (responseDb.isSuccess && !responseDb.getSuccessOrNull().isNullOrEmpty()) {
                            val result = handleSuccessOrFailure(result = responseDb, replaceOrAdd = false)
                            emit(result)
                        }

                        (action == FavoritesAction.LoadNextFavoritesAction && !responseDb.getSuccessOrNull().isNullOrEmpty()) ||
                            (action !in listOf(FavoritesAction.LoadNextFavoritesAction, FavoritesAction.ReLoadFavoritesAction))
                    }

                    val response: Either<Failure, List<FavoriteUi>> =
                        repositoryFavorites.getFavorites(page = values.first() as Int, limit = values[1] as Int)
                            .coMapSuccess { domain ->
                                val resultDb = saveAllDb(domain)
                                mapperFavorites.mapRemoteListToUi(domain)
                            }
                    val result = handleSuccessOrFailure(result = response, replaceOrAdd = replaceOrAdd)
                    emit(result)

                    if (action in listOf(FavoritesAction.ReLoadFavoritesAction, FavoritesAction.LoadFavoritesAction)) {
                        repositoryDbFavorites.removeUnFavorites()
                    }
                }

                FavoritesAction.AddItemAction -> {
                    val idItem = values.first().toString()
                    emit(FavoritesResult.Loading)
                    val response: Either<Failure, Triple<String, Long, Boolean>> =
                        repositoryFavorites.addFavorite(FavoriteRequest(id = idItem))
                    val result = handleSuccessOrFailureAdd(result = response)
                    emit(result)

                    repositoryDbFavorites.addToFavorites(imageId = idItem)
                }
                FavoritesAction.RemoveItemAction -> {
                    val idItem = values.first() as Long
                    emit(FavoritesResult.Loading)
                    val response: Either<Failure, Pair<Long, Boolean>> =
                        repositoryFavorites.unFavorite(idItem)
                    val result = handleSuccessOrFailureRemove(result = response)
                    emit(result)

                    repositoryDbFavorites.removeFromFavorites(id = idItem)
                }
            }
        }
    }

    private suspend fun saveAllDb(domain: List<Favorite>): FavoritesResult {
        val saveItem = mapperFavorites.mapDomainListToDb(domain)
        val response = repositoryDbFavorites.saveAllFavorites(list = saveItem)
        return handleSuccessOrFailureDb(response)
    }

    private fun handleSuccessOrFailureDb(result: Either<Failure, Boolean>): FavoritesResult {
        return when (result) {
            is Either.Error -> FavoritesResult.Error(failure = result.error)
            is Either.Success -> FavoritesResult.SuccessSave
        }
    }

    private fun handleSuccessOrFailure(result: Either<Failure, List<FavoriteUi>>, replaceOrAdd: Boolean): FavoritesResult {
        return when (result) {
            is Either.Error -> FavoritesResult.Error(failure = result.error)
            is Either.Success -> FavoritesResult.Success(items = result.success, replaceOrAdd)
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
