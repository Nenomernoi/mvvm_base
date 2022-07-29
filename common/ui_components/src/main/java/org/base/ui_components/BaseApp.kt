package org.base.ui_components

import android.app.Application
import org.kodein.di.DI

abstract class BaseApp : Application() {
    companion object {
        lateinit var di: DI
    }

    override fun onCreate() {
        super.onCreate()
        setUpDI()
    }

    abstract fun setUpDI()
}
