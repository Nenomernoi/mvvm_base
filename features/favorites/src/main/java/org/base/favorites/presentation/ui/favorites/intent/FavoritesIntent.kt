package org.base.favorites.presentation.ui.favorites.intent

import org.base.mvi.MviIntent

sealed class FavoritesIntent : MviIntent {
    object InitialIntent : FavoritesIntent()
    class LoadNextIntent(val page: Int) : FavoritesIntent()
    object AddToFavorites : FavoritesIntent()
    object RemoveFromFavorites : FavoritesIntent()
    object SwipeOnRefresh : FavoritesIntent()
    object LoadLast : FavoritesIntent()
}