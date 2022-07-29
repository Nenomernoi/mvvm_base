package org.base.di.middlewares

import org.base.di.R
import org.base.network.middleware.NetworkMiddleware
import org.base.network.models.exception.NetworkMiddlewareFailure
import org.base.utils.connectivity.ConnectivityUtils
import org.base.utils.resource_provider.ResourceProvider

class ConnectivityMiddleware(
    private val connectivityUtils: ConnectivityUtils,
    private val resourceProvider: ResourceProvider
) : NetworkMiddleware() {

    override val failure: NetworkMiddlewareFailure
        get() = NetworkMiddlewareFailure(
            middleWareExceptionMessage = resourceProvider.getString(R.string.error_no_network_detected)
        )

    override fun isValid(): Boolean = connectivityUtils.isNetworkAvailable()
}
