package org.base.breeds.presentation.ui.breeds.processor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.base.breeds.presentation.ui.breeds.action.BreedsAction
import org.base.breeds.presentation.ui.breeds.result.BreedsResult
import org.base.breeds_data.db.repository.BreedsDbRepository
import org.base.breeds_data.domain.BreedsRepository
import org.base.common.models.domain.Breed
import org.base.common.models.mapper.BreedMapper
import org.base.common.models.presentation.BreedUi
import org.base.functional_programming.Either
import org.base.functional_programming.Failure
import org.base.mvi.MviProcessorHolder

class BreedsProcessorHolder(
    private val repositoryBreeds: BreedsRepository,
    private val repositoryDbBreeds: BreedsDbRepository,
    private val mapperBreeds: BreedMapper
) : MviProcessorHolder<BreedsAction, BreedsResult> {

    override fun processAction(action: BreedsAction, vararg values: Any): Flow<BreedsResult> {
        return flow {

            if (action != BreedsAction.LoadNextBreedsAction) {
                emit(BreedsResult.Loading)
            }

            val replaceOrAdd = if (action == BreedsAction.ReLoadBreedsAction) {
                repositoryDbBreeds.removeAllBreeds()
                false
            } else {

                val responseDb: Either<Failure, List<BreedUi>> =
                    repositoryDbBreeds.getPageBreeds(page = values.first() as Int, limit = values[1] as Int)
                        .coMapSuccess { db ->
                            mapperBreeds.mapDbListToUi(db)
                        }
                if (responseDb.isSuccess && !responseDb.getSuccessOrNull().isNullOrEmpty()) {
                    val result = handleSuccessOrFailure(result = responseDb, replaceOrAdd = false)
                    emit(result)
                }

                (action == BreedsAction.LoadNextBreedsAction && !responseDb.getSuccessOrNull().isNullOrEmpty()) ||
                    (action !in listOf(BreedsAction.LoadNextBreedsAction, BreedsAction.ReLoadBreedsAction))
            }

            val response: Either<Failure, List<BreedUi>> =
                repositoryBreeds.getBreeds(page = values.first() as Int, limit = values[1] as Int)
                    .coMapSuccess { domain ->
                        val resultDb = saveAllDb(domain)
                        mapperBreeds.mapDomainListToUi(domain)
                    }
            val result = handleSuccessOrFailure(result = response, replaceOrAdd = replaceOrAdd)
            emit(result)
        }
    }

    private suspend fun saveAllDb(domain: List<Breed>): BreedsResult {
        val saveItem = mapperBreeds.mapDomainListToDb(domain)
        val response = repositoryDbBreeds.saveAllBreeds(list = saveItem)
        return handleSuccessOrFailureDb(response)
    }

    private fun handleSuccessOrFailureDb(result: Either<Failure, Boolean>): BreedsResult {
        return when (result) {
            is Either.Error -> BreedsResult.Error(failure = result.error)
            is Either.Success -> BreedsResult.SuccessSave
        }
    }

    private fun handleSuccessOrFailure(result: Either<Failure, List<BreedUi>>, replaceOrAdd: Boolean): BreedsResult {
        return when (result) {
            is Either.Error -> BreedsResult.Error(failure = result.error)
            is Either.Success -> BreedsResult.Success(items = result.success, replaceOrAdd)
        }
    }
}
