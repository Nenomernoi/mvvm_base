package org.mainsoft.base.activity

import androidx.navigation.findNavController
import org.mainsoft.base.R
import org.mainsoft.base.activity.base.BaseActivity

class SplashActivity : BaseActivity() {

    override fun getLayout() = R.layout.activity_splash

    override fun onSupportNavigateUp() = findNavController(R.id.splash_nav_host_fragment).navigateUp()
}