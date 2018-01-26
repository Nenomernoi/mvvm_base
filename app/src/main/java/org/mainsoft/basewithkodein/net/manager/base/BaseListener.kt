package org.mainsoft.basewithkodein.net.manager.base

interface BaseListener {

    fun showHideProgress(isShowHide: Boolean)

    fun showError(text: String)

    fun onLoaded()
}