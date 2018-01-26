package org.mainsoft.basewithkodein.screen.fragment.base

import android.os.Bundle

abstract class BaseWithoutRefreshListFragment<T : Any> : BaseMainListFragment<T>() {

    open fun openItemScreen(bundle: Bundle) {
        //
    }

    override fun getNewData() {
        //
    }
}
