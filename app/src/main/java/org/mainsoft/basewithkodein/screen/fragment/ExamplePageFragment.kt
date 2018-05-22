package org.mainsoft.basewithkodein.screen.fragment

import kotlinx.android.synthetic.main.fragment_base_page.vpMain
import org.kodein.direct
import org.kodein.generic.instance
import org.mainsoft.basewithkodein.App
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.adapter.ExamplePageAdapter
import org.mainsoft.basewithkodein.screen.fragment.base.BasePageFragment
import org.mainsoft.basewithkodein.screen.presenter.ExamplePagePresenter
import org.mainsoft.basewithkodein.screen.view.ExamplePageView

class ExamplePageFragment : BasePageFragment(), ExamplePageView {

    init {
        presenter = App.kodein.direct.instance<ExamplePageView, ExamplePagePresenter>(arg = this)
    }

    override fun initData() {
        activityCallback.setTitle(R.string.app_name)
    }

    override fun initAdapter() {
        pageAdapter = ExamplePageAdapter(childFragmentManager)
    }

    override fun setOffscreenPageLimit() {
        vpMain?.offscreenPageLimit = 3
    }

}