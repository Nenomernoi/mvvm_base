package org.mainsoft.base.screen.fragment

import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import kotlinx.android.synthetic.main.fragment_splash.*
import org.mainsoft.base.R
import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.screen.fragment.base.BaseFragment
import org.mainsoft.base.screen.model.splash.SplashViewModel
import org.mainsoft.base.screen.model.splash.SplashViewModelFactory
import org.mainsoft.base.screen.model.splash.SplashViewState
import org.mainsoft.base.util.navigate

class SplashFragment : BaseFragment() {

    override fun getLayout(): Int = R.layout.fragment_splash

    override fun initData() {
        viewModel = ViewModelProviders.of(this, SplashViewModelFactory).get()
    }

    override fun initListeners() {
        super.initListeners()

        getViewModel<SplashViewModel>()
                .getStore<ViewStateStore<SplashViewState>>()
                .observe(this) {

                    if (it.openNext) {
                        openNext()
                        return@observe
                    }
                    startAnim()
                }
    }

    private fun startAnim() {
        imgLoad?.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.pulse))
    }

    private fun openNext() {
        navigate(R.id.action_splashFragment_to_breedFragment)
    }

}
