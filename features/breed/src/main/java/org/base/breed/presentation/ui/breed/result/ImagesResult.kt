package org.base.breed.presentation.ui.breed.result

import org.base.common.models.presentation.BreedFullUi
import org.base.common.models.presentation.ImageUi
import org.base.functional_programming.Failure
import org.base.mvi.MviResult

sealed class ImagesResult : MviResult {
    data class Success(val items: List<ImageUi>, val replaceOrAdd: Boolean) : ImagesResult()
    data class Error(val failure: Failure) : ImagesResult()
    object Loading : ImagesResult()
    object SuccessSave : ImagesResult()
    data class SetModel(val model: BreedFullUi?) : ImagesResult()
    data class SuccessAdd(val id: String, val idNew: Long, val status: Boolean) : ImagesResult()
    data class SuccessRemove(val id: Long, val status: Boolean) : ImagesResult()
}
