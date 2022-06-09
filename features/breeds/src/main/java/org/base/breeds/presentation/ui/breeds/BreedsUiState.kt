package org.base.breeds.presentation.ui.breeds

import org.base.common.models.presentation.BreedUi
import org.base.main.functional_programming.Failure
import org.base.mvi.Status
import org.base.ui_components.ui.BaseListViewState
import org.base.utils.OneTimeEvent

data class BreedsUiState(
    override val status: Status = Status.NONE,
    override val data: MutableList<BreedUi> = mutableListOf(),
    override val page: Int = 0,
    override val error: OneTimeEvent<Failure>? = null
) : BaseListViewState<BreedUi>()
