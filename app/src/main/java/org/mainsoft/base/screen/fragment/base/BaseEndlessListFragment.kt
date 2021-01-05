package org.mainsoft.base.screen.fragment.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import org.mainsoft.base.listeners.EndlessScrollListener

abstract class BaseEndlessListFragment<T : Any> : BaseListFragment<T>() {

    protected var endLess: EndlessScrollListener? = null

    override fun initData() {
        super.initData()
        endLess = object : EndlessScrollListener(rvMain?.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadNext(page)
            }
        }
    }

    override fun setItems(data: MutableList<T>, page: Int) {
        if (adapter?.list.isNullOrEmpty() && page != 0) {
            endLess?.setCurrentPage(page, data.size - 1)
        }
        super.setItems(data, page)
    }

    protected open fun loadNext(page: Int) {
        //
    }

    override fun clearItems() {
        super.clearItems()
        endLess?.resetState()
    }

    override fun afterAddAdapter() {
        rvMain?.addOnScrollListener(endLess ?: return)
    }
}