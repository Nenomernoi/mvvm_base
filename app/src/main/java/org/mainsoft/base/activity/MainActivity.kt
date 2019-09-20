package org.mainsoft.base.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.mainsoft.base.R
import org.mainsoft.base.activity.base.BaseAnimationActivity
import org.mainsoft.base.screen.fragment.BreedFragment
import org.mainsoft.base.screen.fragment.BreedsFragment

class MainActivity : BaseAnimationActivity() {

    override fun initData() {
        toolbar?.title = getString(R.string.app_name)
    }

    override fun startNewTask() {
        openRootFragment(BreedFragment::class.java, intent.extras ?: Bundle())
      //  openRootFragment(BreedsFragment::class.java, intent.extras ?: Bundle())
    }
}