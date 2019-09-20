package org.mainsoft.base.screen.fragment.base

import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import org.mainsoft.base.R
import org.mainsoft.base.adapter.base.BaseSupportAdapter

abstract class BaseListFragment<T : Any> : BaseFragment() {

    protected var adapter: BaseSupportAdapter<T>? = null

    override fun getLayout(): Int = R.layout.fragment_list

    override fun initData() {
        initRclView()
    }

    protected open fun initRclView() {
        rvMain?.layoutManager = LinearLayoutManager(activity)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    protected open fun setData(data: MutableList<T>, page: Int) {
        if (rvMain?.adapter == null) {
            initAdapter()
            rvMain?.adapter = adapter
            afterAddAdapter()
        } else {
            setItems(data, page)
        }
    }

    protected open fun afterAddAdapter() {
        //
    }

    protected open fun setItems(data: MutableList<T>, page: Int) {
        adapter?.list = data
    }

    protected open fun addItems(data: MutableList<T>) {
        adapter?.list?.addAll(data)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun tryLoadAgain() {
        clearItems()
        loadData()
    }

    protected open fun clearItems() {
        adapter?.list?.clear()
    }

    //////////////////////////////////////////////////////////////////////////////////////

    protected abstract fun loadData()
    protected abstract fun onReloadData()
    protected abstract fun initAdapter()
}