package org.mainsoft.base.screen.model.images

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.net.Repository
import org.mainsoft.base.net.response.Image
import org.mainsoft.base.screen.model.base.BaseListState
import org.mainsoft.base.screen.model.base.BaseViewModel

data class ImagesViewState(
        override val data: MutableList<Image> = mutableListOf(),
        override val page: Int = 0,
        override val loading: Boolean = false,
        val isTopFirst: Boolean = true,
        override val error: Throwable? = null)
    : BaseListState<Image>()

class ImagesViewModel(private val useCase: ImagesUseCase) : BaseViewModel() {

    var id: String? = null
        set(value) {
            field = value
            loadData()
        }

    init {
        store = ViewStateStore(ImagesViewState())
    }

    override fun loadData() {
        loadNext()
    }

    fun loadNext() {
        getStore<ViewStateStore<ImagesViewState>>().dispatchActions(useCase.getItems(getState(), id ?: return))
    }

    fun swipe(vote: Int) {
        getStore<ViewStateStore<ImagesViewState>>().dispatchActions(useCase.swipe(getState(), vote))
    }

    override fun getState() = getStore<ViewStateStore<ImagesViewState>>().state()

}

object ImagesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ImagesViewModel(ImagesUseCase()) as T
}
