package org.mainsoft.basewithkodein.screen.view.base

interface BasePageView<T : Any> : BaseNoTitlePageView {
    fun setAdapter(tabsName: MutableList<T>)
}
