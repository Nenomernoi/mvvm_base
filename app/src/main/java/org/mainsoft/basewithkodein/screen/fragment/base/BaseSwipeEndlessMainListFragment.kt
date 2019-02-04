package org.mainsoft.basewithkodein.screen.fragment.base

import kotlinx.android.synthetic.main.fragment_list.*
import org.mainsoft.basewithkodein.R

abstract class BaseSwipeEndlessMainListFragment<T : Any> : BaseEndlessMainListFragment<T>() {

    override fun getLayout() = R.layout.fragment_list

    override fun initData() {
        super.initData()
        initSwipeRefresh()
    }

    protected open fun refresh(isShow: Boolean) {
        sRefresh.isRefreshing = isShow
    }

    protected open fun initSwipeRefresh() {
        sRefresh.setOnRefreshListener {
            refresh(false)
            onUpdateScreen()
        }
    }
}
