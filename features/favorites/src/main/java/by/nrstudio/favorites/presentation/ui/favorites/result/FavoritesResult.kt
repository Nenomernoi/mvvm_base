package by.nrstudio.favorites.presentation.ui.favorites.result

import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.common.models.presentation.FavoriteUi
import by.nrstudio.mvi.MviResult

sealed class FavoritesResult : MviResult {
    data class Success(val items: List<FavoriteUi>) : FavoritesResult()
    data class SuccessAdd(val id: String, val idNew: Long, val status: Boolean) : FavoritesResult()
    data class SuccessRemove(val id: Long, val status: Boolean) : FavoritesResult()
    data class Error(val failure: Failure) : FavoritesResult()
    object Loading : FavoritesResult()
}
