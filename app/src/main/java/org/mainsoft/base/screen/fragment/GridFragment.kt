package org.mainsoft.base.screen.fragment

import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import kotlinx.android.synthetic.main.fragment_list.*
import org.mainsoft.base.adapter.GridAdapter
import org.mainsoft.base.adapter.base.SpannedGridLayoutManager
import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.screen.fragment.base.BaseEndlessListFragment
import org.mainsoft.base.screen.model.grid.GridViewModel
import org.mainsoft.base.screen.model.grid.GridViewModelFactory
import org.mainsoft.base.screen.model.grid.GridViewState

class GridFragment : BaseEndlessListFragment<String>() {

    override fun initData() {
        viewModel = ViewModelProviders.of(this, GridViewModelFactory).get()
        super.initData()
    }

    override fun initListeners() {
        super.initListeners()

        getViewModel<GridViewModel>()
                .getStore<ViewStateStore<GridViewState>>()
                .observe(this) {

                    showHideProgress(it.loading)

                    showHideNoData(it.error != null)
                    showMessageError(it.error?.message)

                    setData(it.data, it.page)
                }
    }

    override fun initAdapter() {
        adapter = GridAdapter(getViewModel())
        rvMain?.addOnScrollListener(endLess ?: return)
        rvMain?.setHasFixedSize(true)
    }

    override fun loadData() {
        getViewModel<GridViewModel>().loadData()
    }

    override fun loadNext(page: Int) {
        getViewModel<GridViewModel>().loadNext(page)
    }

    override fun initRclView() {
        val manager = SpannedGridLayoutManager(activity,
                SpannedGridLayoutManager.GridSpanLookup { position ->
                    when (position % 18) {
                        0, 10 -> SpannedGridLayoutManager.SpanInfo(2, 2)
                        else -> SpannedGridLayoutManager.SpanInfo(1, 1)
                    }
                },
                3,  // number of columns
                1f // default size of item
        )
        rvMain?.layoutManager = manager
    }

    override fun onReloadData() {
        clearItems()
        getViewModel<GridViewModel>().loadData()
    }
}