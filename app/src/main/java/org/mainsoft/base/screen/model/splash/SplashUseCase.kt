package org.mainsoft.base.screen.model.splash

import android.Manifest
import androidx.fragment.app.Fragment
import com.github.florent37.runtimepermission.kotlin.PermissionException
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import org.mainsoft.base.lib.Action
import org.mainsoft.base.screen.model.base.BaseUseCase

class SplashUseCase : BaseUseCase() {

    fun startWork(): ReceiveChannel<Action<SplashViewState>> = produceActions {
        send { copy(startAnim = true, openNext = false) }
        delay(1500L)
        send { copy(startAnim = false, openNext = true) }
    }

    fun getPermissions(fr: Fragment): ReceiveChannel<Action<SplashViewState>> = produceActions {
        send { copy(startAnim = true, openNext = false) }
        try {
            fr.askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            delay(1500L)
            send { copy(startAnim = false, openNext = true) }

        } catch (e: PermissionException) {
            val builder = StringBuilder()

            if (e.hasDenied()) {
                e.denied.forEach {
                    builder.append(it)
                    builder.append("\n")
                }
                send { copy(startAnim = true, openNext = false, message = builder.toString()) }
                e.askAgain()
            }

            if (e.hasForeverDenied()) {
                e.foreverDenied.forEach {
                    builder.append(it)
                    builder.append("\n")
                }
                send { copy(startAnim = true, openNext = false, message = builder.toString()) }
                e.goToSettings()
            }
        }
    }

}