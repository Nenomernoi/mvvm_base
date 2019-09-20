package org.mainsoft.base

import android.app.Application
import android.content.Context
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.mainsoft.base.db.Db
import org.mainsoft.base.net.Api
import org.mainsoft.base.net.ApiRest

class App : Application() {

    private val settingModule = Kodein.Module(name = "APP") {
        bind<Api>() with singleton { ApiRest.getApi() }
        bind<Db>() with singleton { Db.getInstance(this@App) }

    }

    companion object {
        lateinit var kodein: Kodein
    }

    override fun onCreate() {
        super.onCreate()
        kodein = Kodein {
            import(settingModule)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // MultiDex.install(this)
    }
}