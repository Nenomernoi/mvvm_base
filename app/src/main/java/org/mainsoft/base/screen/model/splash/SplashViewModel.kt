package org.mainsoft.base.screen.model.splash

import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.screen.model.base.BaseViewModel

data class SplashViewState(
        val startAnim: Boolean = true,
        val openNext: Boolean = false)

class SplashViewModel(private val useCase: SplashUseCase) : BaseViewModel() {

    init {
        store = ViewStateStore(SplashViewState())
        loadData()
    }

    override fun loadData() {
        getStore<ViewStateStore<SplashViewState>>().dispatchActions(useCase.startWork())
    }

    override fun getState() = getStore<ViewStateStore<SplashViewState>>().state()

}
