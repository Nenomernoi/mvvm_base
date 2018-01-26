package org.mainsoft.basewithkodein

import android.app.Application
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.factory
import com.github.salomonbrys.kodein.singleton
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.objectbox.BoxStore
import io.reactivex.disposables.CompositeDisposable
import org.mainsoft.basewithkodein.net.Api
import org.mainsoft.basewithkodein.net.ApiRest
import org.mainsoft.basewithkodein.net.response.MyObjectBox
import org.mainsoft.basewithkodein.screen.presenter.MainListPresenter
import org.mainsoft.basewithkodein.screen.presenter.MainPagePresenter
import org.mainsoft.basewithkodein.screen.presenter.MainPresenter
import org.mainsoft.basewithkodein.screen.view.MainListView
import org.mainsoft.basewithkodein.screen.view.MainPageView
import org.mainsoft.basewithkodein.screen.view.MainView
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
       // bind<MainPresenter>() with factory { view: MainView -> MainPresenter(view) }
       // bind<MainListPresenter>() with factory { view: MainListView -> MainListPresenter(view) }
        bind<MainPagePresenter>() with factory { view: MainPageView -> MainPagePresenter(view) }
    }

    companion object {
        lateinit var kodein: Kodein
    }

    override fun onCreate() {
        super.onCreate()
        RxPaparazzo.register(this)
        kodein = Kodein {
            import(settingModule)
            import(screenModule)
        }
    }

}