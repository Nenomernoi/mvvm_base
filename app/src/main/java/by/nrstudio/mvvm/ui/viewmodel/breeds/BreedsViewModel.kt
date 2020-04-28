package by.nrstudio.mvvm.ui.viewmodel.breeds

import android.view.View
import androidx.lifecycle.*
import by.nrstudio.mvvm.db.Db
import by.nrstudio.mvvm.net.Repository
import by.nrstudio.mvvm.net.response.Breed
import by.nrstudio.mvvm.ui.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BreedsViewModel(private val api: Repository, private val db: Db) : ViewModel() {

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
			db.breedDao().deleteAll()
		}
	}

	fun onLoadNext(page: Int) {
		viewModelScope.launch(Dispatchers.Main) {
			try {

				if (page == 0) {
					_status.value = Status.LOADING
				}

				launch(Dispatchers.IO) {
					breedList.addAll(getItems(page))
					_breeds.postValue(breedList)
					_status.postValue(Status.DONE)
				}

			} catch (e: Exception) {
				_status.value = Status.ERROR
			}
		}
	}

	private suspend fun getItems(page: Int): MutableList<Breed> {
		var items = db.breedDao().getItems(page * Repository.LIMIT_PAGE, Repository.LIMIT_PAGE)
		if (items.isNullOrEmpty()) {
			items = api.loadBreeds(page).toMutableList()
			db.breedDao().insert(items)
		}
		return items
	}
}

@Suppress("UNCHECKED_CAST")
class BreedsViewModelFactory(private val repository: Repository, private val db: Db) : ViewModelProvider.NewInstanceFactory() {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return BreedsViewModel(repository, db) as T
	}
}

