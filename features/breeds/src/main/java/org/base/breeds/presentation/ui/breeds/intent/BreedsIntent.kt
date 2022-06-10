package org.base.breeds.presentation.ui.breeds.intent

import org.base.mvi.MviIntent

sealed class BreedsIntent : MviIntent {
    object InitialIntent : BreedsIntent()
    class LoadNextIntent(val page: Int) : BreedsIntent()
    object SwipeOnRefreshIntent : BreedsIntent()
    object LoadLastIntent : BreedsIntent()
}