package by.nrstudio.mvvm.ui.viewmodel.breeds

import android.view.View
import androidx.lifecycle.*
import by.nrstudio.mvvm.net.Repository
import by.nrstudio.mvvm.net.response.Breed
import by.nrstudio.mvvm.ui.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BreedsViewModel(private val repository: Repository) : ViewModel() {

	private var breedList: MutableList<Breed> = mutableListOf()
	private val _breeds = MutableLiveData<MutableList<Breed>>()
	val breeds: LiveData<MutableList<Breed>>
		get() = _breeds

	private val _status = MutableLiveData<Status>()
	val status: LiveData<Status>
		get() = _status

	fun isError() = if (status.value == Status.ERROR) View.VISIBLE else View.GONE

	init {
		onLoadNext(0)
	}

	fun clearData() {
		viewModelScope.launch(Dispatchers.Main) {
			breedList = mutableListOf()
			_breeds.value = breedList
			launch(Dispatchers.IO) {
				repository.clearData()
			}
		}
	}

	fun onLoadNext(page: Int) {
		viewModelScope.launch(Dispatchers.Main) {
			try {

				if (page == 0) {
					_status.value = Status.LOADING
				}

				launch(Dispatchers.IO) {
					breedList.addAll(repository.loadBreeds(page))
					_breeds.postValue(breedList)
					_status.postValue(Status.DONE)
				}

			} catch (e: Exception) {
				_status.value = Status.ERROR
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

