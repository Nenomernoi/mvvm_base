package org.base.breeds.presentation.ui.breeds.result

import org.base.common.models.presentation.BreedUi
import org.base.main.functional_programming.Failure
import org.base.mvi.MviResult

sealed class BreedsResult : MviResult {
    data class Success(val items: List<BreedUi>) : BreedsResult()
    data class Error(val failure: Failure) : BreedsResult()
    object Loading : BreedsResult()
}
