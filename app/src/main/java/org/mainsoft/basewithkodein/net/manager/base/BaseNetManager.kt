package org.mainsoft.basewithkodein.net.manager.base

import org.mainsoft.basewithkodein.net.Api

open class BaseNetManager(val api: Api, val listener: BaseListener) {

    protected open val consumerError = { th: Throwable ->
        listener.showError(th.toString())
    }

    protected open val complite = {
        listener.onLoaded()
    }

}