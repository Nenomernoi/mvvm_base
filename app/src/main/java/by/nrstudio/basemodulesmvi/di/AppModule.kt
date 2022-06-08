package by.nrstudio.basemodulesmvi.di

import android.content.Context
import by.nrstudio.basemodulesmvi.App
import by.nrstudio.basemodulesmvi.util.connectivity.ConnectivityUtilsImpl
import by.nrstudio.basemodulesmvi.util.resource_provider.ResourceProviderImpl
import by.nrstudio.utils.connectivity.ConnectivityUtils
import by.nrstudio.utils.resource_provider.ResourceProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.multiton
import org.kodein.di.generic.singleton

val appModule = Kodein.Module(name = "AppModule") {
    bind<Context>() with multiton { app: App ->
        app.applicationContext
    }
    bind<ResourceProvider>() with singleton { ResourceProviderImpl(instance()) }
    bind<ConnectivityUtils>() with singleton { ConnectivityUtilsImpl(instance()) }
}
