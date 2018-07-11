package org.mainsoft.basewithkodein.activity

import org.mainsoft.basewithkodein.activity.base.BaseActivity
import org.mainsoft.basewithkodein.screen.fragment.ExamplePageFragment

class ExampleActivity : BaseActivity() {

    override fun startNewTask() {
        openRootFragment(ExamplePageFragment::class.java, intent.extras)
    }

}
