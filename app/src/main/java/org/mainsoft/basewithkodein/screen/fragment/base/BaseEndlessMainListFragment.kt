package org.mainsoft.basewithkodein.screen.fragment.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.fragment_list.*
import org.mainsoft.basewithkodein.listener.EndlessScrollListener

abstract class BaseEndlessMainListFragment<T : Any> : BaseMainListFragment<T>() {

    protected var endLess: EndlessScrollListener? = null

    override fun initData() {
        super.initData()
        initEndless()
    }

    protected open fun initEndless() {
        endLess = object : EndlessScrollListener(rvMain?.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadNext()
            }
        }
    }

    override fun initAdapter() {
        if (endLess != null) {
            rvMain?.addOnScrollListener(endLess!!)
        }
    }

    override fun getNewData() {
        endLess?.resetState()
        super.getNewData()
    }

    override fun setItems(data: MutableList<T>) {
        endLess?.setLoading(false)
        adapter?.addItems(data)
    }

    protected open fun loadNext() {
        //
    }

}
