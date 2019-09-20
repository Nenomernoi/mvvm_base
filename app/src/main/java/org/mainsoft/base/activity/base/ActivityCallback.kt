package org.mainsoft.base.activity.base

import android.content.Intent
import android.os.Bundle
import org.mainsoft.base.screen.fragment.base.BaseFragment

interface ActivityCallback {

    fun openFragment(fragmentClass: Class<out BaseFragment>, addToBackStack: Boolean, args: Bundle?)
    fun openRootFragment(fragmentClass: Class<out BaseFragment>, args: Bundle?)

    fun openNewActivity(intent: Intent)
    fun openResultActivity(intent: Intent, code: Int)

    fun hideSoftKeyboard()

}
