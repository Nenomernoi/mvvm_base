package org.mainsoft.base.screen.model.breeds

import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.screen.model.base.BaseRefreshListState
import org.mainsoft.base.screen.model.base.BaseViewModel

data class BreedsViewState(
        override val data: MutableList<Breed> = mutableListOf(),
        override val page: Int = 0,
        override val loading: Boolean = false,
        override val refresh: Boolean = false,
        override val error: Throwable? = null)
    : BaseRefreshListState<Breed>()

class BreedsViewModel(private val useCase: BreedsUseCase) : BaseViewModel() {

    init {
        store = ViewStateStore(BreedsViewState())
        loadData()
    }

    override fun loadData() {
        loadNext(0)
    }

    fun updateItem(position: Int){
        getStore<ViewStateStore<BreedsViewState>>().dispatchAction {
            useCase.updateItem(it, position)
        }
    }

    fun reloadData() {
        getStore<ViewStateStore<BreedsViewState>>().dispatchActions(useCase.clearData())
    }

    fun loadNext(page: Int) {
        getStore<ViewStateStore<BreedsViewState>>().dispatchActions(useCase.getList(getState(), page))
    }

    fun addToFavorite(position: Int) {
        getStore<ViewStateStore<BreedsViewState>>().dispatchAction {
            useCase.addToFavorite(it, position)
        }
    }

    override fun getState() = getStore<ViewStateStore<BreedsViewState>>().state()

}

