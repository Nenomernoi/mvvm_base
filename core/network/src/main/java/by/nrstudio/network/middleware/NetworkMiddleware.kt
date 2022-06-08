package by.nrstudio.network.middleware

import by.nrstudio.network.models.exception.NetworkMiddlewareFailure

abstract class NetworkMiddleware {

    abstract val failure: NetworkMiddlewareFailure

    abstract fun isValid(): Boolean
}
