package org.base.breed.presentation.ui.breed.action

import org.base.mvi.MviAction

sealed class ImagesAction : MviAction {
    object LoadFavoritesAction : ImagesAction()
    object LoadNextFavoritesAction : ImagesAction()
    object ReLoadLastFavoritesAction : ImagesAction()
    object AddItemAction : ImagesAction()
    object RemoveItemAction : ImagesAction()
}
