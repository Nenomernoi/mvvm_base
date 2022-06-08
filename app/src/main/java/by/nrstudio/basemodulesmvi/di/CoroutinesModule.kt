package by.nrstudio.basemodulesmvi.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory

val coroutinesModule = Kodein.Module(name = "CoroutinesModule") {

    bind<CoroutineDispatcher>() with factory { value: String ->
        when (value) {
            "defaultDispatcher" -> providesDefaultDispatcher()
            "ioDispatcher" -> providesIoDispatcher()
            "mainDispatcher" -> providesMainDispatcher()
            else -> providesDefaultDispatcher()
        }
    }
}

internal fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

internal fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

internal fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
