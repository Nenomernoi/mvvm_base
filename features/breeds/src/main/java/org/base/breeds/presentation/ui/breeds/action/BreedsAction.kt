package org.base.breeds.presentation.ui.breeds.action

import org.base.mvi.MviAction

sealed class BreedsAction : MviAction {
    object LoadBreedsAction : BreedsAction()
    object ReLoadBreedsAction : BreedsAction()
    object LoadNextBreedsAction : BreedsAction()
    object ReLoadLastBreedsAction : BreedsAction()
}
