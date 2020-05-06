package by.nrstudio.mvvm

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import by.nrstudio.mvvm.db.Db
import by.nrstudio.mvvm.net.ApiRest
import by.nrstudio.mvvm.net.Repository
import by.nrstudio.mvvm.ui.viewmodel.breeds.BreedViewModelFactory
import by.nrstudio.mvvm.ui.viewmodel.breeds.BreedsViewModelFactory
import by.nrstudio.mvvm.ui.viewmodel.splash.SplashViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {
	override val kodein = Kodein.lazy {
		import(androidXModule(this@App))

		bind() from singleton { ApiRest.getApi() }
		bind() from singleton { Db.getInstance(this@App) }
		bind() from singleton { Repository(instance(), instance()) }

		bind() from provider { SplashViewModelFactory() }
		bind() from provider { BreedsViewModelFactory(instance()) }
		bind() from provider { BreedViewModelFactory(instance()) }
	}

	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)
		MultiDex.install(this)
	}
}
