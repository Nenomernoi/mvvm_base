package org.base.breeds.presentation.ui.breeds.processor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.base.breeds.domain.BreedsRepository
import org.base.breeds.presentation.ui.breeds.action.BreedsAction
import org.base.breeds.presentation.ui.breeds.result.BreedsResult
import org.base.common.models.mapper.BreedMapper
import org.base.common.models.presentation.BreedUi
import org.base.main.functional_programming.Either
import org.base.main.functional_programming.Failure
import org.base.mvi.MviProcessorHolder

class BreedsProcessorHolder(
    private val repositoryBreeds: BreedsRepository,
    private val mapperBreeds: BreedMapper
) : MviProcessorHolder<BreedsAction, BreedsResult> {

    override fun processAction(action: BreedsAction, vararg values: Any): Flow<BreedsResult> {
        return flow {
            when (action) {
                BreedsAction.LoadBreedsAction, BreedsAction.ReLoadLastBreedsAction, BreedsAction.LoadNextBreedsAction -> {
                    if (action != BreedsAction.LoadNextBreedsAction) emit(BreedsResult.Loading)
                    val response: Either<Failure, List<BreedUi>> =
                        repositoryBreeds.getBreeds(page = values.first() as Int, limit = values[1] as Int)
                            .coMapSuccess { domain ->
                                mapperBreeds.mapRemoteListToUi(domain)
                            }
                    val result = handleSuccessOrFailure(result = response)
                    emit(result)
                }
            }
        }
    }

    private fun handleSuccessOrFailure(result: Either<Failure, List<BreedUi>>): BreedsResult {
        return when (result) {
            is Either.Error -> BreedsResult.Error(failure = result.error)
            is Either.Success -> BreedsResult.Success(items = result.success)
        }
    }
}
