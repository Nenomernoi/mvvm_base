package by.nrstudio.ui_components.ui

import androidx.lifecycle.ViewModel
import by.nrstudio.mvi.MviAction
import by.nrstudio.mvi.MviIntent
import by.nrstudio.mvi.MviViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<I : MviIntent, A : MviAction, S : BaseViewState> : ViewModel(), MviViewModel<I, A, S> {

    abstract fun getState(): MutableStateFlow<S>

    override val uiState: StateFlow<S>
        get() = getState().asStateFlow()

    protected val actions = Channel<A>()

    abstract fun subscribeActions()
}

abstract class BaseItemViewModel<T : Any, I : MviIntent, A : MviAction, S : BaseItemViewState<T>> :
    BaseViewModel<I, A, S>()

abstract class BaseEndlessListViewModel<T : Any, I : MviIntent, A : MviAction, S : BaseListViewState<T>> :
    BaseListViewModel<T, I, A, S>(),
    MviViewModel<I, A, S> {
    abstract fun setPage(page: Int)
}

abstract class BaseListViewModel<T : Any, I : MviIntent, A : MviAction, S : BaseListViewState<T>> :
    BaseViewModel<I, A, S>(),
    MviViewModel<I, A, S> {
    companion object {
        const val PAGE_SIZE = 15
    }

    abstract fun clearItems()
}
