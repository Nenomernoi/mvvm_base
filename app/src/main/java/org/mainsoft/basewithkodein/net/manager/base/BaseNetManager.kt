package org.mainsoft.basewithkodein.net.manager.base

import org.mainsoft.basewithkodein.net.Api

open class BaseNetManager(val api: Api, val listener: BaseListener) {

    // protected val consumerError = { throwable -> listener.showError(throwable.toString()) }
    protected val complite = { listener.onLoaded() }

}