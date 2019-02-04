package org.mainsoft.basewithkodein.screen.fragment.base

import android.os.Bundle
import org.mainsoft.basewithkodein.adapter.base.BaseAbsAdapter
import org.mainsoft.basewithkodein.screen.presenter.base.BaseListPresenter

abstract class BaseAbsListFragment<T : Any> : BaseListFragment() {

    protected lateinit var adapter: BaseAbsAdapter<T>

    //////////////////////////////////////////////////////////////////////////////////////

    open fun showContent() {
        showHideProgress(false)
        showHideNoData((getPresenter() as? BaseListPresenter<*>)!!.isDataEmpty())
        adapter.notifyDataSetChanged()
    }

    open fun setData(data: MutableList<T>) {
        if (isAdapter()) {
            initAdapter()
            setAdapter()
        } else {
            adapter.setItems(data)
        }
    }

    override fun initData() {
        //
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun onUpdateScreen() {
        adapter.clearData()
        getNewData()
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        (getPresenter() as? BaseListPresenter<*>)!!.onSaveInstanceState(outState)
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    protected abstract fun setAdapter()
    protected abstract fun isAdapter(): Boolean
}
