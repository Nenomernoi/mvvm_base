package org.base.favorites.presentation.ui.favorites.result

import org.base.common.models.presentation.FavoriteUi
import org.base.main.functional_programming.Failure
import org.base.mvi.MviResult

sealed class FavoritesResult : MviResult {
    data class Success(val items: List<FavoriteUi>, val replaceOrAdd: Boolean) : FavoritesResult()
    data class SuccessAdd(val id: String, val idNew: Long, val status: Boolean) : FavoritesResult()
    data class SuccessRemove(val id: Long, val status: Boolean) : FavoritesResult()
    data class Error(val failure: Failure) : FavoritesResult()
    object Loading : FavoritesResult()
    object SuccessSave : FavoritesResult()
}
