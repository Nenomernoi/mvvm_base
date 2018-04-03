package org.mainsoft.basewithkodein.screen.presenter

import android.content.Context
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.screen.presenter.base.BasePagePresenter
import org.mainsoft.basewithkodein.screen.view.ExamplePageView

class ExamplePagePresenter(view: ExamplePageView) : BasePagePresenter<String>(view) {

    override fun getTabs(mContext: Context): MutableList<String> {
        var tabs: MutableList<String> = mutableListOf()
        tabs.addAll(mContext.resources.getStringArray(R.array.main_tab))
        return tabs
    }

}