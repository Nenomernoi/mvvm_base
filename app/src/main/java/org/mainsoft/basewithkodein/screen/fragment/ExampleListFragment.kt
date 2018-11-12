package org.mainsoft.basewithkodein.screen.fragment

import android.os.Bundle
import org.kodein.di.direct
import org.kodein.di.generic.instance
import org.mainsoft.basewithkodein.App
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.adapter.ExampleAdapter
import org.mainsoft.basewithkodein.base.OnItemClickListener
import org.mainsoft.basewithkodein.net.response.CountryResponse
import org.mainsoft.basewithkodein.screen.fragment.base.BaseMainListFragment
import org.mainsoft.basewithkodein.screen.fragment.base.BaseSwipeMainListFragment
import org.mainsoft.basewithkodein.screen.presenter.ExampleListPresenter
import org.mainsoft.basewithkodein.screen.presenter.base.BasePresenter
import org.mainsoft.basewithkodein.screen.view.ExampleListView

class ExampleListFragment : BaseSwipeMainListFragment<CountryResponse>(), ExampleListView {

    init {
        presenter = App.kodein.direct.instance<ExampleListView, ExampleListPresenter>(arg = this)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    //FOR PAGER
    companion object {
        fun newInstance(position: Int): ExampleListFragment {

            val pageFragment = ExampleListFragment()
            val arguments = Bundle()
            arguments.putLong(BasePresenter.ARGUMENT_PAGE_NUMBER, position.toLong())
            pageFragment.arguments = arguments
            return pageFragment
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun getLayout(): Int {
        return R.layout.fragment_list
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun initAdapter() {
        adapter = ExampleAdapter(getData(), object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                showToastError("position-> $position")
            }
        })
    }

    override fun openItemScreen(bundle: Bundle) {
        openScreen(ExampleListFragment::class.java, bundle)
    }

}
