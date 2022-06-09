package by.nrstudio.favorites.presentation.ui.favorites

import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.common.models.presentation.FavoriteUi
import by.nrstudio.mvi.Status
import by.nrstudio.ui_components.ui.BaseListViewState
import by.nrstudio.utils.OneTimeEvent

data class FavoritesUiState(
    override val status: Status = Status.NONE,
    override val data: MutableList<FavoriteUi> = mutableListOf(),
    override val page: Int = 0,
    override val error: OneTimeEvent<Failure>? = null,
    val changeItem: Int = -1,
) : BaseListViewState<FavoriteUi>()
