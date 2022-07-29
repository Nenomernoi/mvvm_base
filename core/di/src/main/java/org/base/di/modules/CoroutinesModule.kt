package org.base.di.modules

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory

val coroutinesModule = DI.Module(name = "CoroutinesModule") {

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
