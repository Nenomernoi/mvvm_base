package org.mainsoft.basewithkodein.screen.fragment.base

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_list.txtNoData

abstract class BaseListFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData()
        super.onViewCreated(view, savedInstanceState)
    }

    open fun showHideNoData(isEmpty: Boolean) {
        txtNoData.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    protected abstract fun getNewData()
    protected abstract fun initAdapter()
    protected abstract fun onUpdateScreen()
}
