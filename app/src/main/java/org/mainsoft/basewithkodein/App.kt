package org.mainsoft.basewithkodein

import android.app.Application
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.objectbox.BoxStore
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.singleton
import org.mainsoft.basewithkodein.net.Api
import org.mainsoft.basewithkodein.net.ApiRest
import org.mainsoft.basewithkodein.net.response.MyObjectBox
import org.mainsoft.basewithkodein.screen.presenter.ExampleListPresenter
import org.mainsoft.basewithkodein.screen.presenter.ExamplePagePresenter
import org.mainsoft.basewithkodein.screen.presenter.ExamplePresenter
import org.mainsoft.basewithkodein.screen.view.ExampleListView
import org.mainsoft.basewithkodein.screen.view.ExamplePageView
import org.mainsoft.basewithkodein.screen.view.ExampleView
import org.mainsoft.basewithkodein.util.PresenterUtil
import org.mainsoft.basewithkodein.util.Setting

class App : Application() {

    private val settingModule = Kodein.Module {
        bind<Setting>() with singleton { Setting(this@App) }
        bind<Api>() with singleton { ApiRest.getApi() }
        bind<PresenterUtil>() with singleton { PresenterUtil() }
        bind<CompositeDisposable>() with singleton { CompositeDisposable() }
        bind<BoxStore>() with singleton { initDb() }
    }

    private val presenterUtil = PresenterUtil()

    private fun initDb() = MyObjectBox.builder()
            .androidContext(this@App)
            .directory(File(filesDir.parent, "${packageName}_db"))
            .build()

    private val screenModule = Kodein.Module {
        bind<ExamplePresenter>() with factory { view: ExampleView -> presenterUtil.getPresenter<ExamplePresenter>(view) }
        bind<ExampleListPresenter>() with factory { view: ExampleListView -> presenterUtil.getPresenter<ExampleListPresenter>(view) }
        bind<ExamplePagePresenter>() with factory { view: ExamplePageView -> presenterUtil.getPresenter<ExamplePagePresenter>(view) }
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