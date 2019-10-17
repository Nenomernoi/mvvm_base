package org.mainsoft.base.screen.model.splash

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.screen.model.base.BaseViewModel

data class SplashViewState(
        val startAnim: Boolean = true,
        val message: String? = null,
        val openNext: Boolean = false)

class SplashViewModel(private val useCase: SplashUseCase) : BaseViewModel() {

    init {
        store = ViewStateStore(SplashViewState())
    }

    override fun loadData() {
        getStore<ViewStateStore<SplashViewState>>().dispatchActions(useCase.startWork())
    }

    fun getPermission(fr: Fragment) {
        getStore<ViewStateStore<SplashViewState>>().dispatchActions(useCase.getPermissions(fr))
    }

    override fun getState() = getStore<ViewStateStore<SplashViewState>>().state()

}

object SplashViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SplashViewModel(SplashUseCase()) as T
}