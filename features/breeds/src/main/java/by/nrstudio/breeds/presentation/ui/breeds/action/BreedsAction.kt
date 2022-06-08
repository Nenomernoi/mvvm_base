package by.nrstudio.breeds.presentation.ui.breeds.action

import by.nrstudio.mvi.MviAction

sealed class BreedsAction : MviAction {
    object LoadBreedsAction : BreedsAction()
    object LoadNextBreedsAction : BreedsAction()
    object ReLoadLastBreedsAction : BreedsAction()
}
