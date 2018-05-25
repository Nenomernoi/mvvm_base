package org.mainsoft.basewithkodein.activity.base

import android.content.Intent
import android.os.Bundle
import org.mainsoft.basewithkodein.screen.fragment.base.BaseFragment

interface ActivityCallback {
    fun openFragment(fragmentClass: Class<out BaseFragment>, addToBackStack: Boolean, args: Bundle)
    fun openRootFragment(fragmentClass: Class<out BaseFragment>, args: Bundle)
    fun openNewActivity(intent: Intent)
    fun openResultActivity(intent: Intent, code: Int)
    fun hideSoftKeyboard()
    fun getLocation()
    fun initLocation()
    fun setTitle(title: Any)
    fun openPermission(listener: PermissionListener, vararg permissions: String)
    fun openPermissionBase(isRepeat: Boolean, listener: PermissionListener, vararg permissions: String)
}