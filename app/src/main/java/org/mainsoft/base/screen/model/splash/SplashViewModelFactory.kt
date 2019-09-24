package org.mainsoft.base.screen.model.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object SplashViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SplashViewModel(SplashUseCase()) as T
}