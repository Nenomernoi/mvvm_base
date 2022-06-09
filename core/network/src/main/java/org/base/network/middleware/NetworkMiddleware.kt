package org.base.network.middleware

import org.base.network.models.exception.NetworkMiddlewareFailure

abstract class NetworkMiddleware {

    abstract val failure: NetworkMiddlewareFailure

    abstract fun isValid(): Boolean
}
