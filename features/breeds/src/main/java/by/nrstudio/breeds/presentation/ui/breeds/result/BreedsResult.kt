package by.nrstudio.breeds.presentation.ui.breeds.result

import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.common.models.presentation.BreedUi
import by.nrstudio.mvi.MviResult

sealed class BreedsResult : MviResult {
    data class Success(val items: List<BreedUi>) : BreedsResult()
    data class Error(val failure: Failure) : BreedsResult()
    object Loading : BreedsResult()
}
