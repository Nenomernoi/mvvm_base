package org.mainsoft.basewithkodein.screen.presenter.base

import android.content.Context
import android.os.Bundle
import io.objectbox.BoxStore
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.kodein.generic.instance
import org.mainsoft.basewithkodein.App
import org.mainsoft.basewithkodein.net.Api
import org.mainsoft.basewithkodein.net.manager.base.BaseNetManager
import org.mainsoft.basewithkodein.screen.fragment.base.BaseFragment
import org.mainsoft.basewithkodein.screen.view.base.BaseView
import org.mainsoft.basewithkodein.util.Setting

abstract class BasePresenter(val view: BaseView) : Presenter {

    companion object {
        const val ARGUMENT_PAGE_NUMBER = "page"
        const val ARGUMENT_ID = "id"
        const val ARGUMENT_LONGITUDE = "longitude"
        const val ARGUMENT_LATITUDE = "latitude"
        const val ARGUMENT_EXTRA_ID = "extraId"

        const val EMPTY = -1.0
        const val EMPTY_INT = -1
    }

    protected val api: Api by App.kodein.instance()

    protected val db: BoxStore by App.kodein.instance()

    protected val setting: Setting by App.kodein.instance()

    private val compositeDisposable: CompositeDisposable by App.kodein.instance()

    protected var netManager: BaseNetManager? = null

    protected var dis: Disposable? = null

    ////////////////////////////////////////////////////////////////////////////////////////

    protected var latitude: Double? = null
    protected var longitude: Double? = null

    ////////////////////////////////////////////////////////////////////////////////////////

    open fun readyLocation(lat: Double, lng: Double, distance: Double) {
        this.latitude = lat
        this.longitude = lng
    }

    open fun errorLocation() {
        //
    }

    open fun getLocation() {
        getView<BaseView>()?.getLocation()
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    fun getLng() = longitude
    fun getLat() = latitude
    fun isEmptyLocation(): Boolean = longitude == null || latitude == null

    ////////////////////////////////////////////////////////////////////////////////////////

    protected fun addSubscription(dis: Disposable?) {
        if (dis == null) {
            return
        }
        compositeDisposable.add(dis)
    }

    protected fun stopSubscription(dis: Disposable?) {
        if (dis == null) {
            return
        }
        compositeDisposable.remove(dis)
    }

    protected fun onAllRemoveSubs() {
        compositeDisposable.clear()
    }

    override fun onStop() {
        netManager?.listener = null
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    open fun onLoaded() {
        showHideProgress(false)
    }

    fun onError(e: Exception) {
        view.showError(e.localizedMessage)
        showHideProgress(false)
    }

    fun showHideProgress(isShowHide: Boolean) {
        view.showHideProgress(isShowHide)
    }

    open fun showError(e: String) {
        view.showError(e)
        showHideProgress(false)
    }

    fun <V : Any> getView(): V? = if ((view as? BaseFragment)!!.isAdded) (view as? V)!!
    else null

    ////////////////////////////////////////////////////////////////////////////////////

    abstract fun initData(context: Context, bundle: Bundle?, arguments: Bundle?)

}
