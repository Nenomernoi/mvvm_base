package org.mainsoft.basewithkodein.screen.presenter.base

import android.content.Context
import android.os.Bundle
import org.mainsoft.basewithkodein.screen.view.base.BaseNoTitlePageView

abstract class BaseNoTitlePagePresenter(view: BaseNoTitlePageView) : BasePresenter(view) {

    protected open var pageOpen = 0

    override fun initData(context: Context, bundle: Bundle?, arguments: Bundle?) {
        initTab(context)
        getView<BaseNoTitlePageView>()?.openPage(pageOpen)
    }

    protected abstract fun initTab(context: Context)
}
