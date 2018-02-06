package org.mainsoft.basewithkodein.screen.fragment

import android.os.Bundle
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.adapter.MainAdapter
import org.mainsoft.basewithkodein.base.OnItemClickListener
import org.mainsoft.basewithkodein.net.response.CountryResponse
import org.mainsoft.basewithkodein.screen.fragment.base.BaseMainListFragment
import org.mainsoft.basewithkodein.screen.presenter.MainListPresenter
import org.mainsoft.basewithkodein.screen.presenter.base.BasePresenter
import org.mainsoft.basewithkodein.screen.view.MainListView

class MainListFragment : BaseMainListFragment<CountryResponse>(), MainListView {

    init {
       // presenter = App.kodein.factory<MainListView, MainListPresenter>() as MainListPresenter
        presenter = MainListPresenter(this)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    //FOR PAGER
    companion object {
        fun newInstance(position: Int): MainListFragment {

            val pageFragment = MainListFragment()
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
        adapter = MainAdapter(getPresenter<MainListPresenter>()!!.getData(), object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                getPresenter<MainListPresenter>()?.openItemScreen(position)
            }
        })
    }

    override fun openItemScreen(bundle: Bundle) {
        openScreen(MainListFragment::class.java, bundle)
    }

    override fun getNewData() {
        getPresenter<MainListPresenter>()?.clearData()
        getPresenter<MainListPresenter>()?.firstLoad()
    }

}