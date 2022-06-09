package by.nrstudio.favorites.presentation.ui.favorites.action

import by.nrstudio.mvi.MviAction

sealed class FavoritesAction : MviAction {
    object LoadFavoritesAction : FavoritesAction()
    object AddItemAction : FavoritesAction()
    object RemoveItemAction : FavoritesAction()
    object LoadNextFavoritesAction : FavoritesAction()
    object ReLoadLastFavoritesAction : FavoritesAction()
}
