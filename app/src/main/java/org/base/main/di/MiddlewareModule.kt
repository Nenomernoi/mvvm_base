import org.base.main.middlewares.ConnectivityMiddleware
import org.base.main.middlewares.provider.MiddlewareProviderImpl
import org.base.network.middleware.provider.MiddlewareProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val middleWareModule: Kodein.Module = Kodein.Module(name = "MiddleWareModule") {
    bind<MiddlewareProvider>() with singleton {
        MiddlewareProviderImpl.Builder()
            .add(
                middleware = ConnectivityMiddleware(
                    connectivityUtils = instance(),
                    resourceProvider = instance()
                )
            )
            .build()
    }
}
