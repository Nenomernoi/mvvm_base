package org.mainsoft.basewithkodein

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.fabric.sdk.android.Fabric
import io.objectbox.BoxStore
import io.reactivex.disposables.CompositeDisposable
import org.kodein.Kodein
import org.kodein.generic.bind
import org.kodein.generic.factory
import org.kodein.generic.singleton
import org.mainsoft.basewithkodein.net.Api
import org.mainsoft.basewithkodein.net.ApiRest
import org.mainsoft.basewithkodein.net.response.MyObjectBox
import org.mainsoft.basewithkodein.screen.presenter.ExampleListPresenter
import org.mainsoft.basewithkodein.screen.presenter.ExamplePagePresenter
import org.mainsoft.basewithkodein.screen.presenter.ExamplePresenter
import org.mainsoft.basewithkodein.screen.view.ExampleListView
import org.mainsoft.basewithkodein.screen.view.ExamplePageView
import org.mainsoft.basewithkodein.screen.view.ExampleView
import org.mainsoft.basewithkodein.util.Setting

class App : Application() {

    private val settingModule = Kodein.Module {
        bind<Setting>() with singleton { Setting(this@App) }
        bind<Api>() with singleton { ApiRest.getApi() }
        bind<CompositeDisposable>() with singleton { CompositeDisposable() }
        bind<BoxStore>() with singleton { initDb() }
    }

    private fun initDb() = MyObjectBox.builder().androidContext(this@App).build()

    private val screenModule = Kodein.Module {
        bind<ExamplePresenter>() with factory { view: ExampleView -> ExamplePresenter(view) }
        bind<ExampleListPresenter>() with factory { view: ExampleListView -> ExampleListPresenter(view) }
        bind<ExamplePagePresenter>() with factory { view: ExamplePageView -> ExamplePagePresenter(view) }
    }

    companion object {
        lateinit var kodein: Kodein
    }

    override fun onCreate() {
        super.onCreate()
        RxPaparazzo.register(this)
       // Fabric.with(this, Crashlytics())
        kodein = Kodein {
            import(settingModule)
            import(screenModule)
        }
    }

}