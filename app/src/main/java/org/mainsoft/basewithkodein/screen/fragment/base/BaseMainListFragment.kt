package org.mainsoft.basewithkodein.screen.fragment.base

import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.adapter.base.BaseSupportAdapter
import org.mainsoft.basewithkodein.screen.presenter.base.BaseListPresenter

abstract class BaseMainListFragment<T : Any> : BaseListFragment() {

    protected var adapter: BaseSupportAdapter<T>? = null

    override fun getLayout(): Int = R.layout.fragment_list

    //////////////////////////////////////////////////////////////////////////////////////

    open fun showContent() {
        Handler().postDelayed({

            showHideNoData(adapter == null || adapter?.itemCount == 0)
            showHideProgress(false)
            adapter?.notifyDataSetChanged()

        }, 300)
    }

    open fun setData(data: MutableList<T>) {
        if (rvMain?.adapter == null) {
            initAdapter()
            rvMain?.adapter = adapter
        } else {
            setItems(data)
        }
    }

    protected fun getData(): MutableList<T> = getPresenter<BaseListPresenter<T>>()?.getData()
            ?: mutableListOf()

    protected open fun setItems(data: MutableList<T>) {
        adapter?.setItems(data)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    open fun clearSearch() {
        adapter?.clearData()
        adapter?.notifyDataSetChanged()
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun getNewData() {
        getPresenter<BaseListPresenter<*>>()?.clearData()
        getPresenter<BaseListPresenter<*>>()?.firstLoad()
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun onUpdateScreen() {
        adapter?.clearData()
        getNewData()
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun initData() {
        initRclView()
        super.initData()
    }

    protected open fun initRclView() {
        rvMain?.layoutManager = LinearLayoutManager(activity)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        (getPresenter() as? BaseListPresenter<*>)!!.onSaveInstanceState(outState)
    }
}