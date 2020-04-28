package by.nrstudio.mvvm.ui.viewmodel.splash

import androidx.lifecycle.*
import by.nrstudio.mvvm.ui.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

	private val _status = MutableLiveData<Status>()
	val status: LiveData<Status>
		get() = _status

	init {
		onWork()
	}

	private fun onWork() {
		viewModelScope.launch(Dispatchers.Main) {
			try {
				_status.value = Status.LOADING
				delay(3000L)
				_status.value = Status.DONE
			} catch (e: Exception) {
				_status.value = Status.ERROR
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

