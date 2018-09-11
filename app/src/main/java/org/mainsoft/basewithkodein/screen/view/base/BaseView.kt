package org.mainsoft.basewithkodein.screen.view.base

import android.content.DialogInterface

interface BaseView {
    fun showError(error: String)
    fun showHideProgress(isShowHide: Boolean)
    fun showMessageError(message: String?)
    fun showMessageError(message: String?, listener: DialogInterface.OnClickListener)
    fun showMessageError(message: Int)
    fun showMessageError(message: Int, listener: DialogInterface.OnClickListener)
    fun getLocation()
    fun firstLoad()
}
