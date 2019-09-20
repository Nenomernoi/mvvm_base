package org.mainsoft.base.screen.model

import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.screen.model.base.BaseViewModel
import org.mainsoft.base.screen.model.base.BreedsRefreshListState

data class BreedsViewState(
        override val data: MutableList<Breed> = mutableListOf(),
        override val page: Int = 0,
        override val loading: Boolean = false,
        override val refresh: Boolean = false,
        override val error: Throwable? = null)
    : BreedsRefreshListState<Breed>()

class BreedsViewModel(private val useCase: BreedsUseCase) : BaseViewModel() {

    init {
        store = ViewStateStore(BreedsViewState())
        loadData()
    }

    override fun loadData() {
        loadNext(0)
    }

    fun reloadData() {
        getStore<ViewStateStore<BreedsViewState>>().dispatchActions(useCase.clearData())
    }

    fun loadNext(page: Int) {
        getStore<ViewStateStore<BreedsViewState>>().dispatchActions(useCase.getList(getStore<ViewStateStore<BreedsViewState>>().state(), page))
    }

    fun showFullDescription(position: Int) {
        getStore<ViewStateStore<BreedsViewState>>().dispatchAction {
            useCase.showFull(it, position)
        }
    }

    fun openMode(position: Int) {

    }

}

