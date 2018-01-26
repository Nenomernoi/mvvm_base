package org.mainsoft.basewithkodein.screen.presenter.base

import android.content.Context
import org.mainsoft.basewithkodein.screen.view.base.BasePageView

abstract class BasePagePresenter<T : Any>(view: BasePageView<T>) : BaseNoTitlePagePresenter(view) {

    protected var tabs: MutableList<String> = mutableListOf()

    override fun initTab(context: Context) {
        getView<BasePageView<T>>()?.setAdapter(getTabs(context))
    }

    ////////////////////////////////////////////////////////////////////////////////

    protected abstract fun getTabs(mContext: Context): MutableList<T>
}
