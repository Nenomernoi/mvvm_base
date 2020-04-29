package by.nrstudio.mvvm.ui.viewmodel.breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.nrstudio.mvvm.net.Repository
import by.nrstudio.mvvm.net.response.Breed
import by.nrstudio.mvvm.ui.Status
import by.nrstudio.mvvm.ui.viewmodel.base.BaseListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BreedsViewModel(private val repository: Repository) : BaseListViewModel<Breed>() {

	init {
		onLoadNext(0)
	}

	override suspend fun baseClearData() {
		repository.clearData()
	}

	override fun onLoadNext(page: Int) {
		viewModelScope.launch(Dispatchers.Main) {
			try {

				if (page == 0) {
					changeState(Status.LOADING)
				}

				launch(Dispatchers.IO) {
					setData(repository.loadBreeds(page))
					changeState(Status.DONE, ioThread = true)
				}
			} catch (e: Exception) {
				changeState(Status.ERROR, error = e)
			}
		}
	}
}

@Suppress("UNCHECKED_CAST")
class BreedsViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return BreedsViewModel(repository) as T
	}
}
