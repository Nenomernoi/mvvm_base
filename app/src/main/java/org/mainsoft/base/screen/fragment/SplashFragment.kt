package org.mainsoft.base.screen.fragment

import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import kotlinx.android.synthetic.main.fragment_splash.*
import org.mainsoft.base.R
import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.screen.fragment.base.BaseFragment
import org.mainsoft.base.screen.model.splash.SplashViewModel
import org.mainsoft.base.screen.model.splash.SplashViewModelFactory
import org.mainsoft.base.screen.model.splash.SplashViewState
import org.mainsoft.base.util.navigateSplash

class SplashFragment : BaseFragment() {

    override fun getLayout(): Int = R.layout.fragment_splash

    override fun initData() {
        viewModel = ViewModelProviders.of(this, SplashViewModelFactory).get()
        checkPermmissions()
    }

    private fun checkPermmissions() {
        getViewModel<SplashViewModel>().getPermission(this)
    }

    override fun initListeners() {
        super.initListeners()

        imgLoad?.setOnClickListener {
            checkPermmissions()
        }

        getViewModel<SplashViewModel>()
                .getStore<ViewStateStore<SplashViewState>>()
                .observe(this) {

                    it.message?.let { text ->
                        showMessage(text)
                    }

                    if (it.openNext) {
                        openNext()
                        return@observe
                    }
                    startAnim()
                }
    }

    private fun showMessage(message: String) {
        if (message.isEmpty()) {
            return
        }
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun startAnim() {
        imgLoad?.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.pulse))
    }

    private fun openNext() {
        navigateSplash(R.id.action_splashFragment_to_breedFragment)
    }

}
