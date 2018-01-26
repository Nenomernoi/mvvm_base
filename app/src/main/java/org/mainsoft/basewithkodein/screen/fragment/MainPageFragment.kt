package org.mainsoft.basewithkodein.screen.fragment

import com.github.salomonbrys.kodein.factory
import kotlinx.android.synthetic.main.fragment_base_page.vpMain
import org.mainsoft.basewithkodein.App
import org.mainsoft.basewithkodein.adapter.MainPageAdapter
import org.mainsoft.basewithkodein.screen.fragment.base.BasePageFragment
import org.mainsoft.basewithkodein.screen.presenter.MainPagePresenter
import org.mainsoft.basewithkodein.screen.view.MainPageView

class MainPageFragment : BasePageFragment(), MainPageView {

    init {
        presenter = App.kodein.factory<MainPageView, MainPagePresenter>() as MainPagePresenter
        presenter = MainPagePresenter(this)
    }

    override fun initAdapter() {
        pageAdapter = MainPageAdapter(childFragmentManager)
    }

    override fun setOffscreenPageLimit() {
        vpMain?.offscreenPageLimit = 2
    }

}