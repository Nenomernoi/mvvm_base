package org.mainsoft.basewithkodein.screen.view.base

import android.content.DialogInterface

interface BaseView {
    fun showHideProgress(isShowHide: Boolean)
    fun showError(error: String)
    fun showToastError(message: Any?)
    fun showMessageError(message: Any?, listener: DialogInterface.OnClickListener?)
    fun getLocation()
    fun firstLoad()
}
