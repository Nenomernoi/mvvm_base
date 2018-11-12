package org.mainsoft.basewithkodein.screen.fragment.base

import kotlinx.android.synthetic.main.fragment_list.*

abstract class BaseSwipeMainListFragment<T : Any> : BaseMainListFragment<T>() {

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
