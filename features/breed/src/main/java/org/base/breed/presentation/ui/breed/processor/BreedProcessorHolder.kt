package org.base.breed.presentation.ui.breed.processor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.base.breed.presentation.ui.breed.action.ImagesAction
import org.base.breed.presentation.ui.breed.result.ImagesResult
import org.base.breed_data.db.repository.ImageDbRepository
import org.base.breed_data.domain.ImagesRepository
import org.base.breeds_data.db.repository.BreedsDbRepository
import org.base.common.models.data.FavoriteRequest
import org.base.common.models.domain.Favorite
import org.base.common.models.domain.Image
import org.base.common.models.mapper.BreedMapper
import org.base.common.models.mapper.FavoriteMapper
import org.base.common.models.mapper.ImageMapper
import org.base.common.models.presentation.BreedFullUi
import org.base.common.models.presentation.ImageUi
import org.base.favorites_data.db.repository.FavoriteDbRepository
import org.base.favorites_data.domain.FavoritesRepository
import org.base.main.functional_programming.Either
import org.base.main.functional_programming.Failure
import org.base.mvi.MviProcessorHolder

class BreedProcessorHolder(
    private val repositoryFavorites: FavoritesRepository,
    private val repositoryImages: ImagesRepository,
    private val repositoryDbFavorites: FavoriteDbRepository,
    private val repositoryDbImages: ImageDbRepository,
    private val repositoryDbBreeds: BreedsDbRepository,
    private val mapperBreed: BreedMapper,
    private val mapperFavorites: FavoriteMapper,
    private val mapperImages: ImageMapper
) : MviProcessorHolder<ImagesAction, ImagesResult> {

    override fun processAction(action: ImagesAction, vararg values: Any): Flow<ImagesResult> {
        return flow {
            when (action) {
                ImagesAction.LoadFavoritesAction,
                ImagesAction.ReLoadLastFavoritesAction,
                ImagesAction.LoadNextFavoritesAction -> {
                    if (action != ImagesAction.LoadNextFavoritesAction) {
                        emit(ImagesResult.Loading)
                    }

                    val breedId = values.first().toString()
                    val page = values[1] as Int
                    val limit = values[2] as Int

                    if (action == ImagesAction.LoadFavoritesAction) {
                        val responseDb: Either<Failure, BreedFullUi?> =
                            repositoryDbBreeds.getBreed(breedId = breedId)
                                .coMapSuccess { db ->
                                    db?.let { mapperBreed.mapDbToFullUi(it) }
                                }

                        if (responseDb.isSuccess && responseDb.getSuccessOrNull() != null) {
                            val result = handleModelSuccessOrFailure(result = responseDb)
                            emit(result)
                        }
                    }

                    val responseDb: Either<Failure, List<ImageUi>> =
                        repositoryDbImages.getPageImages(breedId = breedId, page = page as Int, limit = limit)
                            .coMapSuccess { db ->
                                mapperImages.mapDbListToUi(db)
                            }
                    if (responseDb.isSuccess && !responseDb.getSuccessOrNull().isNullOrEmpty()) {
                        val result = handleSuccessOrFailure(result = responseDb, replaceOrAdd = false)
                        emit(result)
                    }

                    val replaceOrAdd = (action == ImagesAction.LoadNextFavoritesAction && !responseDb.getSuccessOrNull().isNullOrEmpty()) ||
                        (action != ImagesAction.LoadNextFavoritesAction)

                    val response: Either<Failure, List<ImageUi>> =
                        repositoryImages.getImages(breedId = breedId, page = page, limit = limit)
                            .coMapSuccess { domain ->
                                val resultDb = saveAllDb(breedId, domain)
                                mapperImages.mapRemoteListToUi(domain)
                            }
                    emit(handleSuccessOrFailure(result = response, replaceOrAdd = replaceOrAdd))
                }

                ImagesAction.AddItemAction -> {
                    val image = values.first() as ImageUi
                    val breedId = values[1].toString()
                    emit(ImagesResult.Loading)

                    val response: Either<Failure, Triple<String, Long, Boolean>> =
                        repositoryFavorites.addFavorite(FavoriteRequest(id = image.id))
                    val result = handleSuccessOrFailureAdd(result = response)
                    emit(result)

                    if (result is ImagesResult.SuccessAdd) {
                        val imageUi = image.apply {
                            this.idFavorite = result.idNew
                        }
                        emit(saveFavoriteImageDb(imageUi))
                        emit(saveItemDb(breedId, imageUi))
                    }
                }

                ImagesAction.RemoveItemAction -> {
                    val image = values.first() as ImageUi
                    val breedId = values[1].toString()

                    emit(ImagesResult.Loading)
                    val response: Either<Failure, Pair<Long, Boolean>> =
                        repositoryFavorites.unFavorite(image.idFavorite)
                    val result = handleSuccessOrFailureRemove(result = response)
                    emit(result)

                    if (result is ImagesResult.SuccessRemove) {
                        repositoryDbFavorites.removeFromFavorites(id = image.idFavorite)
                        val imageUi = image.apply {
                            this.idFavorite = 0L
                        }
                        emit(saveItemDb(breedId, imageUi))
                    }
                }
            }
        }
    }

    private suspend fun saveAllDb(breedId: String, domain: List<Image>): ImagesResult {
        val saveItem = mapperImages.mapDomainListToDb(domain, breedId)
        val response = repositoryDbImages.saveAllImages(list = saveItem)
        return handleSuccessOrFailureDb(response)
    }

    private suspend fun saveItemDb(breedId: String, ui: ImageUi): ImagesResult {
        val saveItem = mapperImages.mapUiToDb(ui, breedId)
        val response = repositoryDbImages.saveImage(item = saveItem)
        return handleSuccessOrFailureDb(response)
    }

    private suspend fun saveFavoriteImageDb(ui: ImageUi): ImagesResult {
        val saveItem = mapperFavorites.mapDomainToDb(
            Favorite(
                id = ui.idFavorite,
                imageId = ui.id,
                image = ui.url,
                isFavorite = true
            )
        )
        val response = repositoryDbFavorites.saveFavorite(item = saveItem)
        return handleSuccessOrFailureDb(response)
    }

    private fun handleSuccessOrFailureDb(result: Either<Failure, Boolean>): ImagesResult {
        return when (result) {
            is Either.Error -> ImagesResult.Error(failure = result.error)
            is Either.Success -> ImagesResult.SuccessSave
        }
    }

    private fun handleModelSuccessOrFailure(result: Either<Failure, BreedFullUi?>): ImagesResult {
        return when (result) {
            is Either.Error -> ImagesResult.Error(failure = result.error)
            is Either.Success -> ImagesResult.SetModel(model = result.success)
        }
    }

    private fun handleSuccessOrFailure(result: Either<Failure, List<ImageUi>>, replaceOrAdd: Boolean): ImagesResult {
        return when (result) {
            is Either.Error -> ImagesResult.Error(failure = result.error)
            is Either.Success -> ImagesResult.Success(items = result.success, replaceOrAdd)
        }
    }

    private fun handleSuccessOrFailureAdd(result: Either<Failure, Triple<String, Long, Boolean>>): ImagesResult {
        return when (result) {
            is Either.Error -> ImagesResult.Error(failure = result.error)
            is Either.Success -> ImagesResult.SuccessAdd(
                id = result.success.first,
                idNew = result.success.second,
                status = result.success.third
            )
        }
    }

    private fun handleSuccessOrFailureRemove(result: Either<Failure, Pair<Long, Boolean>>): ImagesResult {
        return when (result) {
            is Either.Error -> ImagesResult.Error(failure = result.error)
            is Either.Success -> ImagesResult.SuccessRemove(id = result.success.first, status = result.success.second)
        }
    }
}
