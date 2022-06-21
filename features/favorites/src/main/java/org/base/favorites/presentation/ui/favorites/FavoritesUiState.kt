package org.base.favorites.presentation.ui.favorites

import org.base.common.models.presentation.FavoriteUi
import org.base.functional_programming.Failure
import org.base.mvi.Status
import org.base.ui_components.ui.BaseListViewState
import org.base.utils.OneTimeEvent

data class FavoritesUiState(
    override val status: Status = Status.NONE,
    override val data: MutableList<FavoriteUi> = mutableListOf(),
    override val page: Int = 0,
    override val error: OneTimeEvent<Failure>? = null,
    val changeItem: Int = -1,
) : BaseListViewState<FavoriteUi>()
