package org.mainsoft.base.activity

import androidx.navigation.findNavController
import org.mainsoft.base.R
import org.mainsoft.base.activity.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onSupportNavigateUp() = findNavController(R.id.my_nav_host_fragment).navigateUp()
}