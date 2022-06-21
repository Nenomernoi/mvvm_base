package org.base.breed.presentation.ui.breed

import org.base.common.models.presentation.BreedFullUi
import org.base.common.models.presentation.ImageUi
import org.base.functional_programming.Failure
import org.base.mvi.Status
import org.base.ui_components.ui.BaseListViewState
import org.base.utils.OneTimeEvent

data class BreedUiState(
    override val status: Status = Status.NONE,
    override val data: MutableList<ImageUi> = mutableListOf(),
    override val page: Int = 0,
    override val error: OneTimeEvent<Failure>? = null,
    val model: BreedFullUi? = null,
    val breedId: String = "",
    val changeItem: Int = -1,
) : BaseListViewState<ImageUi>()
