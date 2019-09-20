package org.mainsoft.base.activity

import android.os.Bundle
import org.mainsoft.base.activity.base.BaseAnimationActivity
import org.mainsoft.base.screen.fragment.BreedsFragment

class MainActivity : BaseAnimationActivity() {

    override fun startNewTask() {
        openRootFragment(BreedsFragment::class.java, intent.extras ?: Bundle())
    }
}