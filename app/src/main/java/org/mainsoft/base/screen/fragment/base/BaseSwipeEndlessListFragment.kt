package org.mainsoft.base.screen.fragment.base

import kotlinx.android.synthetic.main.fragment_list_refresh.*
import org.mainsoft.base.R

abstract class BaseSwipeEndlessListFragment<T : Any> : BaseEndlessListFragment<T>() {

    override fun getLayout(): Int = R.layout.fragment_list_refresh

    override fun initData() {
        super.initData()
        initSwipeRefresh()
    }

    protected open  fun showHideRefresh(isShowHide: Boolean){
        activity?.runOnUiThread {
            sRefresh.isRefreshing = isShowHide
        }
    }

    private fun initSwipeRefresh() {
        sRefresh.setOnRefreshListener {
            onReloadData()
        }
    }
}