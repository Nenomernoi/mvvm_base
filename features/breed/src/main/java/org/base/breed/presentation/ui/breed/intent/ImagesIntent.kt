package org.base.breed.presentation.ui.breed.intent

import org.base.mvi.MviIntent

sealed class ImagesIntent : MviIntent {
    object InitialIntent : ImagesIntent()
    class LoadNextIntent(val page: Int) : ImagesIntent()
    object LoadLastIntent : ImagesIntent()
    object AddToFavoritesIntent : ImagesIntent()
    object RemoveFromFavoritesIntent : ImagesIntent()
}
