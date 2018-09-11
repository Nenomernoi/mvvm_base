package org.mainsoft.basewithkodein.net.manager.base

import org.mainsoft.basewithkodein.net.Api

open class BaseNetManager(val api: Api, var listener: BaseListener?) {

    protected open val consumerError = { th: Throwable ->
        if (listener != null) {
            listener?.showError(th.toString())
        }
    }

    protected open val complete = {
        if (listener != null) {
            listener?.onLoaded()
        }
    }

}