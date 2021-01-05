package org.mainsoft.base.screen.fragment.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import org.mainsoft.base.util.rootDestinations

class BaseItemFragment : Fragment() {

    private var navHostId: Int = -1
    private var layoutRes: Int = -1
    private val appBarConfig = AppBarConfiguration(rootDestinations)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            layoutRes = it.getInt(KEY_LAYOUT)
            navHostId = it.getInt(KEY_NAV_HOST)

        } ?: return
    }

    fun onBackPressed(): Boolean {
        return requireActivity()
                .findNavController(navHostId)
                .navigateUp(appBarConfig)
    }


    fun popToRoot() {
        val navController = requireActivity().findNavController(navHostId)
        navController.popBackStack(navController.graph.startDestination, false)
    }

    fun handleDeepLink(intent: Intent) = requireActivity().findNavController(navHostId).handleDeepLink(intent)

    companion object {

        private const val KEY_LAYOUT = "layout_key"
        private const val KEY_NAV_HOST = "nav_host_key"

        fun newInstance(layoutRes: Int, navHostId: Int, bundle: Bundle? = Bundle()) = BaseItemFragment().apply {
            arguments = bundle?.apply {
                putInt(KEY_NAV_HOST, navHostId)
                putInt(KEY_LAYOUT, layoutRes)
            }
        }
    }
}