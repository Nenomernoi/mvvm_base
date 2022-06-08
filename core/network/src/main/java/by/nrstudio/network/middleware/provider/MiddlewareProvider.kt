package by.nrstudio.network.middleware.provider

import by.nrstudio.network.middleware.NetworkMiddleware

interface MiddlewareProvider {
    fun getAll(): List<NetworkMiddleware>
}
