package by.nrstudio.favorites.presentation.ui.favorites.intent

import by.nrstudio.mvi.MviIntent

sealed class FavoritesIntent : MviIntent {
    object InitialIntent : FavoritesIntent()
    class LoadNextIntent(val page: Int) : FavoritesIntent()
    object AddToFavorites : FavoritesIntent()
    object RemoveFromFavorites : FavoritesIntent()
    object SwipeOnRefresh : FavoritesIntent()
    object LoadLast : FavoritesIntent()
}
