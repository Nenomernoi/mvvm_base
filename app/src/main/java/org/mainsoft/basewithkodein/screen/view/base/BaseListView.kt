package org.mainsoft.basewithkodein.screen.view.base

import android.os.Bundle

interface BaseListView<T : Any> : BaseView {
    fun setData(data: MutableList<T>)
    fun showContent()
    fun openItemScreen(bundle: Bundle)
}

