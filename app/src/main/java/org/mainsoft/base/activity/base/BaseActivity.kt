package org.mainsoft.base.activity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.mainsoft.base.R

abstract class BaseActivity : AppCompatActivity() {

    protected open fun getLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAnim()
        setContentView(getLayout())
        initData()
    }

    override fun onStop() {
        stopAnim()
        super.onStop()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    protected open fun initData() {
        //
    }

    ////////////////////////////////////////////////////////////////////////////////////

   private fun startAnim() {
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right_slow)
    }

    private fun stopAnim() {
        overridePendingTransition(R.anim.slide_out_left_fast, R.anim.slide_in_left)
    }

}
