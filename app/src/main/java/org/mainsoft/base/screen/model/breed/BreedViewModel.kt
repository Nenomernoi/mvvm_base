package org.mainsoft.base.screen.model.breed

import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.screen.model.base.BaseViewModel
import org.mainsoft.base.screen.model.base.BaseViewState

data class BreedViewState(
        val update: Boolean = false,
        override val data: Breed? = null,
        override val loading: Boolean = false,
        override val error: Throwable? = null)
    : BaseViewState<Breed>()

class BreedViewModel(private val useCase: BreedUseCase) : BaseViewModel() {

    init {
        store = ViewStateStore(BreedViewState())
    }

    override fun loadData() {
        getStore<ViewStateStore<BreedViewState>>().dispatchActions(useCase.getItem(getState().data?.id ?: return))
    }

    fun setModel(model: Breed?) {
        getStore<ViewStateStore<BreedViewState>>().dispatchActions(useCase.setModel(model))
    }

    fun addToFavorite() {
        getStore<ViewStateStore<BreedViewState>>().dispatchActions(useCase.addToFavorite(getState()))
    }

    override fun getState() = getStore<ViewStateStore<BreedViewState>>().state()

}
