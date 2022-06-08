package by.nrstudio.breeds.presentation.ui.breeds

import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.common.models.presentation.BreedUi
import by.nrstudio.mvi.Status
import by.nrstudio.ui_components.ui.BaseListViewState
import by.nrstudio.utils.OneTimeEvent

data class BreedsUiState(
    override val status: Status = Status.NONE,
    override val data: MutableList<BreedUi> = mutableListOf(),
    override val page: Int = 0,
    override val error: OneTimeEvent<Failure>? = null
) : BaseListViewState<BreedUi>()
