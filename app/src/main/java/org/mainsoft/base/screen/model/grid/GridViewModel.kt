package org.mainsoft.base.screen.model.grid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.screen.model.base.BaseListState
import org.mainsoft.base.screen.model.base.BaseViewModel

data class GridViewState(
        override val data: MutableList<String> = mutableListOf(),
        override val page: Int = 0,
        val position: Int = 0,
        override val loading: Boolean = false,
        override val error: Throwable? = null)
    : BaseListState<String>()

class GridViewModel(private val useCase: GridUseCase) : BaseViewModel() {

    init {
        store = ViewStateStore(GridViewState())
        loadData()
    }

    override fun loadData() {
        loadNext(0)
    }

    fun loadNext(page: Int) {
        getStore<ViewStateStore<GridViewState>>().dispatchActions(useCase.getList(getState(), page))
    }

    override fun getState() = getStore<ViewStateStore<GridViewState>>().state()

}

object GridViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            GridViewModel(GridUseCase()) as T
}

