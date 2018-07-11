package org.mainsoft.basewithkodein.screen.presenter.base

import android.os.Bundle
import org.mainsoft.basewithkodein.screen.view.base.BaseListView

abstract class BaseListPresenter<T : Any>(view: BaseListView<T>) : BasePresenter(view) {

    companion object {
        const val BUNDLE_LIST_KEY = "BUNDLE_LIST_KEY"
        const val BUNDLE_POSITION = "BUNDLE_POSITION_KEY"
    }

    private var data: MutableList<T> = mutableListOf()

    /////////////////////////////////////////////////////////////////////////////////////////

    fun onSaveInstanceState(outState: Bundle) {
        if (!isDataEmpty()) {
            saveCache(outState)
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    fun isDataEmpty(): Boolean = data.isEmpty()

    fun clearData() {
        data.clear()
    }

    open fun clearSearch() {
        clearData()
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    fun setData(list: MutableList<T>) {
        data = list
    }

    fun getData(): MutableList<T> = data

    fun getItem(position: Int): T = data[position]

    fun addData(list: MutableList<T>) {
        data.addAll(list)
    }

    open fun onLoad(list: MutableList<T>) {
        addData(list)
        getView<BaseListView<T>>()?.setData(list)
    }

    open fun onError(er: Throwable) {
        getView<BaseListView<T>>()?.showError(er.message!!)
        showHideProgress(false)
    }

    open fun onError(text: String) {
        getView<BaseListView<T>>()?.showError(text)
        showHideProgress(false)
    }

    override fun onLoaded() {
        getView<BaseListView<T>>()?.showContent()
    }

    /////////////////////////////////////

    abstract fun firstLoad()
    protected abstract fun loadCache(savedInstanceState: Bundle)
    protected abstract fun saveCache(outState: Bundle)
}
