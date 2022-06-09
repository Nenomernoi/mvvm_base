package by.nrstudio.favorites.presentation.ui.favorites

import androidx.lifecycle.viewModelScope
import by.nrstudio.common.models.presentation.FavoriteUi
import by.nrstudio.favorites.presentation.ui.favorites.action.FavoritesAction
import by.nrstudio.favorites.presentation.ui.favorites.intent.FavoritesIntent
import by.nrstudio.favorites.presentation.ui.favorites.processor.FavoritesProcessorHolder
import by.nrstudio.favorites.presentation.ui.favorites.result.FavoritesResult
import by.nrstudio.mvi.Status
import by.nrstudio.ui_components.ui.BaseEndlessListViewModel
import by.nrstudio.utils.toOneTimeEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class FavoritesViewModel(
    private val actionProcessorHolderFavorites: FavoritesProcessorHolder,
) : BaseEndlessListViewModel<FavoriteUi, FavoritesIntent, FavoritesAction, FavoritesUiState>() {

    private val _uiState = MutableStateFlow(FavoritesUiState())

    override fun getState() = _uiState

    init {
        processIntents(FavoritesIntent.InitialIntent)
        subscribeActions()
    }

    override fun subscribeActions() {
        viewModelScope.launch {
            actions.receiveAsFlow()
                .flatMapLatest {

                    actionProcessorHolderFavorites.processAction(
                        action = it,
                        when (it) {
                            FavoritesAction.LoadNextFavoritesAction, FavoritesAction.ReLoadLastFavoritesAction -> uiState.value.page
                            FavoritesAction.AddItemAction -> uiState.value.data[uiState.value.changeItem].imageId
                            FavoritesAction.RemoveItemAction -> uiState.value.data[uiState.value.changeItem].id
                            else -> 0
                        },
                        PAGE_SIZE
                    )
                }
                .collectLatest { reduce(it) }
        }
    }

    override fun processIntents(intent: FavoritesIntent, vararg values: Any) {
        viewModelScope.launch {
            when (intent) {
                is FavoritesIntent.LoadNextIntent -> setPage(values.first() as Int)
                FavoritesIntent.AddToFavorites, FavoritesIntent.RemoveFromFavorites -> setChangeItem(values.first() as Int)
                FavoritesIntent.LoadLast -> _uiState.value.page
                FavoritesIntent.InitialIntent, FavoritesIntent.SwipeOnRefresh -> clearItems()
            }
            actions.send(mapIntentToAction(intent = intent))
        }
    }

    override fun mapIntentToAction(intent: FavoritesIntent): FavoritesAction {
        return when (intent) {
            FavoritesIntent.InitialIntent -> FavoritesAction.LoadFavoritesAction
            FavoritesIntent.SwipeOnRefresh -> FavoritesAction.LoadFavoritesAction
            FavoritesIntent.LoadLast -> FavoritesAction.ReLoadLastFavoritesAction
            FavoritesIntent.AddToFavorites -> FavoritesAction.AddItemAction
            FavoritesIntent.RemoveFromFavorites -> FavoritesAction.RemoveItemAction
            is FavoritesIntent.LoadNextIntent -> FavoritesAction.LoadNextFavoritesAction
        }
    }

    private fun setChangeItem(position: Int) {
        _uiState.value = uiState.value.copy(changeItem = position)
    }

    override fun setPage(page: Int) {
        _uiState.value = uiState.value.copy(page = page)
    }

    override fun clearItems() {
        _uiState.value = uiState.value.copy(data = mutableListOf(), page = 0)
    }

    private fun reduce(result: FavoritesResult) {
        when (result) {
            is FavoritesResult.SuccessAdd -> {

                val newItems = mutableListOf<FavoriteUi>().apply {
                    addAll(
                        _uiState.value.data.apply {
                            this.first { it.imageId == result.id }.apply {
                                this.isFavorite = true
                                this.id = result.idNew
                            }
                        }
                    )
                }

                _uiState.value = uiState.value.copy(
                    status = Status.DONE,
                    data = newItems,
                    changeItem = -1,
                    error = null
                )
            }
            is FavoritesResult.SuccessRemove -> {
                val newItems = mutableListOf<FavoriteUi>().apply {
                    addAll(
                        _uiState.value.data.apply {
                            this.first { it.id == result.id }.apply {
                                this.isFavorite = false
                            }
                        }
                    )
                }

                _uiState.value = uiState.value.copy(
                    status = Status.DONE,
                    data = newItems,
                    changeItem = -1,
                    error = null
                )
            }
            is FavoritesResult.Success -> {
                val newItems = mutableListOf<FavoriteUi>().apply {
                    addAll(_uiState.value.data)
                    addAll(result.items)
                }
                _uiState.value = uiState.value.copy(
                    status = Status.DONE,
                    data = newItems,
                    error = null
                )
            }
            is FavoritesResult.Error -> {
                _uiState.value = uiState.value.copy(
                    status = Status.ERROR,
                    error = result.failure.toOneTimeEvent()
                )
            }
            FavoritesResult.Loading -> {
                _uiState.value = uiState.value.copy(
                    status = Status.LOADING,
                    error = null
                )
            }
        }
    }
}
