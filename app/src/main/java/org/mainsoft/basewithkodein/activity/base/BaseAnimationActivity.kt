package org.mainsoft.basewithkodein.activity.base

import org.mainsoft.basewithkodein.R

abstract class BaseAnimationActivity : BaseActivity() {

    override fun startAnim() {
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right_slow)
    }

    override fun stopAnim() {
        overridePendingTransition(R.anim.slide_out_left_fast, R.anim.slide_in_left)
    }
}