package org.base.favorites.presentation.ui.favorites.intent

import org.base.mvi.MviIntent

sealed class FavoritesIntent : MviIntent {
    object InitialIntent : FavoritesIntent()
    class LoadNextIntent(val page: Int) : FavoritesIntent()
    object AddToFavoritesIntent : FavoritesIntent()
    object RemoveFromFavoritesIntent : FavoritesIntent()
    object SwipeOnRefreshIntent : FavoritesIntent()
    object LoadLastIntent : FavoritesIntent()
}
