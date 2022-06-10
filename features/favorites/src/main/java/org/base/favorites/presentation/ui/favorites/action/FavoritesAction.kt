package org.base.favorites.presentation.ui.favorites.action

import org.base.mvi.MviAction

sealed class FavoritesAction : MviAction {
    object LoadFavoritesAction : FavoritesAction()
    object ReLoadFavoritesAction : FavoritesAction()
    object AddItemAction : FavoritesAction()
    object RemoveItemAction : FavoritesAction()
    object LoadNextFavoritesAction : FavoritesAction()
    object ReLoadLastFavoritesAction : FavoritesAction()
}
