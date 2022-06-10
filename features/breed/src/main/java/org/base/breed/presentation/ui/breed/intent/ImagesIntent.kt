package org.base.breed.presentation.ui.breed.intent

import org.base.mvi.MviIntent

sealed class ImagesIntent : MviIntent {
    object InitialIntent : ImagesIntent()
    class LoadNextIntent(val page: Int) : ImagesIntent()
    object LoadLast : ImagesIntent()
    object AddToFavorites : ImagesIntent()
    object RemoveFromFavorites : ImagesIntent()
}
