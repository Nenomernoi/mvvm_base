package org.mainsoft.basewithkodein.activity

import android.os.Bundle
import org.mainsoft.basewithkodein.activity.base.BaseActivity
import org.mainsoft.basewithkodein.screen.fragment.MainPageFragment

class MainActivity : BaseActivity() {

    override fun startNewTask() {
        openRootFragment(MainPageFragment::class.java, Bundle())
    }

}
