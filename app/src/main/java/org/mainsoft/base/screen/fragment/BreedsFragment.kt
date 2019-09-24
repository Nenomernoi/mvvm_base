package org.mainsoft.base.screen.fragment

import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import org.mainsoft.base.adapter.BreedListAdapter
import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.screen.fragment.base.BaseSwipeEndlessListFragment
import org.mainsoft.base.screen.model.breeds.BreedsViewModel
import org.mainsoft.base.screen.model.breeds.BreedsViewModelFactory
import org.mainsoft.base.screen.model.breeds.BreedsViewState

class BreedsFragment : BaseSwipeEndlessListFragment<Breed>() {

    override fun initData() {
        viewModel = ViewModelProviders.of(this, BreedsViewModelFactory).get()
        super.initData()
    }

    override fun initListeners() {
        super.initListeners()

        getViewModel<BreedsViewModel>()
                .getStore<ViewStateStore<BreedsViewState>>()
                .observe(this) {

                    showHideProgress(it.loading)
                    showHideRefresh(it.refresh)

                    showHideNoData(it.error != null)
                    showMessageError(it.error?.message)

                    setData(it.data, it.page)
                }
    }

    override fun initAdapter() {
        adapter = BreedListAdapter(getViewModel(), object : BreedsReturnCallback {
            override fun onUpdateItem(position: Int) {
                getViewModel<BreedsViewModel>().updateItem(position)
            }
        })
    }

    override fun loadData() {
        getViewModel<BreedsViewModel>().loadData()
    }

    override fun loadNext(page: Int) {
        getViewModel<BreedsViewModel>().loadNext(page)
    }

    override fun onReloadData() {
        clearItems()
        getViewModel<BreedsViewModel>().reloadData()
    }
}
