package org.base.breed.presentation.ui.breed

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.base.breed.presentation.ui.breed.action.ImagesAction
import org.base.breed.presentation.ui.breed.intent.ImagesIntent
import org.base.breed.presentation.ui.breed.processor.BreedProcessorHolder
import org.base.breed.presentation.ui.breed.result.ImagesResult
import org.base.common.models.presentation.ImageUi
import org.base.mvi.Status
import org.base.ui_components.ui.BaseEndlessListViewModel
import org.base.utils.toOneTimeEvent

@ExperimentalCoroutinesApi
class BreedViewModel(private val actionProcessorHolderBreed: BreedProcessorHolder) :
    BaseEndlessListViewModel<ImageUi, ImagesIntent, ImagesAction, BreedUiState>() {

    private val _uiState = MutableStateFlow(BreedUiState())

    override fun getState() = _uiState

    init {
        subscribeActions()
    }

    override fun subscribeActions() {
        viewModelScope.launch {
            actions.receiveAsFlow()
                .flatMapLatest {
                    actionProcessorHolderBreed.processAction(
                        action = it,
                        uiState.value.breedId,
                        when (it) {
                            ImagesAction.LoadNextFavoritesAction, ImagesAction.ReLoadLastFavoritesAction -> uiState.value.page
                            ImagesAction.AddItemAction -> uiState.value.data[uiState.value.changeItem]
                            ImagesAction.RemoveItemAction -> uiState.value.data[uiState.value.changeItem]
                            else -> 0
                        },
                        PAGE_SMALL_SIZE
                    )
                }
                .collectLatest { reduce(it) }
        }
    }

    override fun processIntents(intent: ImagesIntent, vararg values: Any) {
        viewModelScope.launch {
            when (intent) {
                is ImagesIntent.LoadNextIntent -> setPage(values.first() as Int)
                ImagesIntent.LoadLastIntent -> _uiState.value.page
                ImagesIntent.InitialIntent -> {
                    setModel(values.first() as String)
                    clearItems()
                }
                ImagesIntent.AddToFavoritesIntent, ImagesIntent.RemoveFromFavoritesIntent -> setChangeItem(values.first() as Int)
            }
            actions.send(mapIntentToAction(intent = intent))
        }
    }

    private fun setModel(breedId: String) {
        _uiState.value = uiState.value.copy(breedId = breedId)
    }

    private fun setChangeItem(position: Int) {
        _uiState.value = uiState.value.copy(changeItem = position)
    }

    override fun mapIntentToAction(intent: ImagesIntent): ImagesAction {
        return when (intent) {
            ImagesIntent.InitialIntent -> ImagesAction.LoadFavoritesAction
            ImagesIntent.LoadLastIntent -> ImagesAction.ReLoadLastFavoritesAction
            is ImagesIntent.LoadNextIntent -> ImagesAction.LoadNextFavoritesAction
            ImagesIntent.AddToFavoritesIntent -> ImagesAction.AddItemAction
            ImagesIntent.RemoveFromFavoritesIntent -> ImagesAction.RemoveItemAction
        }
    }

    override fun setPage(page: Int) {
        _uiState.value = uiState.value.copy(page = page)
    }

    override fun clearItems() {
        _uiState.value = uiState.value.copy(data = mutableListOf(), page = 0)
    }

    private fun reduce(result: ImagesResult) {
        when (result) {
            is ImagesResult.SetModel -> {
                _uiState.value = uiState.value.copy(
                    model = result.model,
                    error = null
                )
            }
            is ImagesResult.SuccessAdd -> {

                val newItems = mutableListOf<ImageUi>().apply {
                    addAll(
                        _uiState.value.data.apply {
                            this.first { it.id == result.id }.apply {
                                this.idFavorite = result.idNew
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
            is ImagesResult.SuccessRemove -> {
                val newItems = mutableListOf<ImageUi>().apply {
                    addAll(
                        _uiState.value.data.apply {
                            this.first { it.idFavorite == result.id }.apply {
                                this.idFavorite = 0L
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

            is ImagesResult.SuccessSave -> {
                _uiState.value = uiState.value.copy(
                    status = Status.DONE,
                    error = null
                )
            }

            is ImagesResult.Success -> {
                val newItems = mutableListOf<ImageUi>().apply {
                    addAll(_uiState.value.data)
                    when {
                        result.replaceOrAdd && result.items.isNotEmpty() && this.isNotEmpty() -> {
                            for (i in 0 until this.size) {
                                result.items.firstOrNull { it.id == this[i].id }?.let {
                                    this[i] = it
                                }
                            }
                        }
                        else -> addAll(result.items)
                    }
                }
                _uiState.value = uiState.value.copy(
                    status = Status.DONE,
                    data = newItems,
                    error = null
                )
            }

            is ImagesResult.Error -> {
                _uiState.value = uiState.value.copy(
                    status = Status.ERROR,
                    error = result.failure.toOneTimeEvent()
                )
            }

            ImagesResult.Loading -> {
                _uiState.value = uiState.value.copy(
                    status = Status.LOADING,
                    error = null
                )
            }
        }
    }
}
