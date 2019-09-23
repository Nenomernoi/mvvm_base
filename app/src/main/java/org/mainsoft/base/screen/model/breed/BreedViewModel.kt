package org.mainsoft.base.screen.model.breed

import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.screen.model.base.BaseViewModel
import org.mainsoft.base.screen.model.base.BaseViewState

data class BreedViewState(
        override val data: Breed? = null,
        override val loading: Boolean = false,
        override val error: Throwable? = null)
    : BaseViewState<Breed>()

class BreedViewModel(private val useCase: BreedUseCase) : BaseViewModel() {

    var id: String? = null

    init {
        store = ViewStateStore(BreedViewState())

    }

    override fun loadData() {
        getStore<ViewStateStore<BreedViewState>>().dispatchActions(useCase.getItem(id ?: return))
    }

    override fun getState() = getStore<ViewStateStore<BreedViewState>>().state()

}

