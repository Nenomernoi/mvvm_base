package org.base.breeds.presentation.ui.breeds

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.base.breeds.presentation.ui.breeds.action.BreedsAction
import org.base.breeds.presentation.ui.breeds.intent.BreedsIntent
import org.base.breeds.presentation.ui.breeds.processor.BreedsProcessorHolder
import org.base.breeds.presentation.ui.breeds.result.BreedsResult
import org.base.common.models.presentation.BreedUi
import org.base.mvi.Status
import org.base.ui_components.ui.BaseEndlessListViewModel
import org.base.utils.toOneTimeEvent

@ExperimentalCoroutinesApi
class BreedsViewModel(
    private val actionProcessorHolderBreeds: BreedsProcessorHolder,
) : BaseEndlessListViewModel<BreedUi, BreedsIntent, BreedsAction, BreedsUiState>() {

    private val _uiState = MutableStateFlow(BreedsUiState())

    override fun getState() = _uiState

    init {
        processIntents(BreedsIntent.InitialIntent)
        subscribeActions()
    }

    override fun subscribeActions() {
        viewModelScope.launch {
            actions.receiveAsFlow()
                .flatMapLatest {
                    actionProcessorHolderBreeds.processAction(
                        action = it,
                        when (it) {
                            BreedsAction.LoadNextBreedsAction, BreedsAction.ReLoadLastBreedsAction -> uiState.value.page
                            else -> 0
                        },
                        PAGE_SIZE
                    )
                }
                .collectLatest { reduce(it) }
        }
    }

    override fun processIntents(intent: BreedsIntent, vararg values: Any) {
        viewModelScope.launch {
            when (intent) {
                is BreedsIntent.LoadNextIntent -> setPage(values.first() as Int)
                BreedsIntent.LoadLastIntent -> _uiState.value.page
                BreedsIntent.InitialIntent, BreedsIntent.SwipeOnRefreshIntent -> clearItems()
            }
            actions.send(mapIntentToAction(intent = intent))
        }
    }

    override fun mapIntentToAction(intent: BreedsIntent): BreedsAction {
        return when (intent) {
            BreedsIntent.InitialIntent -> BreedsAction.LoadBreedsAction
            BreedsIntent.SwipeOnRefreshIntent -> BreedsAction.ReLoadBreedsAction
            BreedsIntent.LoadLastIntent -> BreedsAction.ReLoadLastBreedsAction
            is BreedsIntent.LoadNextIntent -> BreedsAction.LoadNextBreedsAction
        }
    }

    override fun setPage(page: Int) {
        _uiState.value = uiState.value.copy(page = page)
    }

    override fun clearItems() {
        _uiState.value = uiState.value.copy(data = mutableListOf(), page = 0)
    }

    private fun reduce(result: BreedsResult) {
        when (result) {
            is BreedsResult.SuccessSave -> {
                _uiState.value = uiState.value.copy(
                    status = Status.DONE,
                    error = null
                )
            }

            is BreedsResult.Success -> {
                val newItems = mutableListOf<BreedUi>().apply {
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

            is BreedsResult.Error -> {
                _uiState.value = uiState.value.copy(
                    status = Status.ERROR,
                    error = result.failure.toOneTimeEvent()
                )
            }

            BreedsResult.Loading -> {
                _uiState.value = uiState.value.copy(
                    status = Status.LOADING,
                    error = null
                )
            }
        }
    }
}
