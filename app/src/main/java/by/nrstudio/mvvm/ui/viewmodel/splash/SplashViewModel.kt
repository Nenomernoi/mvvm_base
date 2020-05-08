package by.nrstudio.mvvm.ui.viewmodel.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.nrstudio.mvvm.ui.Status
import by.nrstudio.mvvm.ui.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : BaseViewModel() {

    companion object {
        private const val ANIM_TIME = 3000L
    }

	init {
		onWork()
	}

	private fun onWork() {
		viewModelScope.launch(Dispatchers.Main) {
			try {
				changeState(Status.LOADING)
				delay(ANIM_TIME)
				changeState(Status.DONE)
			} catch (e: Exception) {
				changeState(Status.ERROR, error = e)
			}
		}
	}
}

@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory() : ViewModelProvider.NewInstanceFactory() {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return SplashViewModel() as T
	}
}
