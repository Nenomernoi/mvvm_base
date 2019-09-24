package org.mainsoft.base.screen.model.splash

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import org.mainsoft.base.lib.Action
import org.mainsoft.base.screen.model.base.BaseUseCase

class SplashUseCase : BaseUseCase() {

    fun startWork(): ReceiveChannel<Action<SplashViewState>> = produceActions {
        send { copy(startAnim = true, openNext = false) }
        delay(3000L)
        send { copy(startAnim = false, openNext = true) }
    }

}