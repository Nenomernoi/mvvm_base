package org.mainsoft.basewithkodein.screen.fragment

import kotlinx.android.synthetic.main.fragment_base_page.vpMain
import org.mainsoft.basewithkodein.adapter.MainPageAdapter
import org.mainsoft.basewithkodein.screen.fragment.base.BasePageFragment
import org.mainsoft.basewithkodein.screen.presenter.MainPagePresenter
import org.mainsoft.basewithkodein.screen.view.MainPageView

class MainPageFragment : BasePageFragment(), MainPageView {

    init {
        presenter = MainPagePresenter(this)
    }

    override fun initAdapter() {
        pageAdapter = MainPageAdapter(childFragmentManager)
    }

    override fun setOffscreenPageLimit() {
        vpMain?.offscreenPageLimit = 2
    }

}