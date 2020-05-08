package by.nrstudio.mvvm.ui.viewmodel.breeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.nrstudio.mvvm.net.Repository
import by.nrstudio.mvvm.net.response.Breed
import by.nrstudio.mvvm.ui.Status
import by.nrstudio.mvvm.ui.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BreedViewModel(private val repository: Repository) : BaseViewModel() {

    private var itemBase: Breed? = null
    private val _item = MutableLiveData<Breed>()
    val item: LiveData<Breed>
        get() = _item

    fun loadData(model: Breed?) {
        if (model == null) {
            changeState(Status.ERROR)
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            try {
                changeState(Status.LOADING)
                setData(model)

                launch(Dispatchers.IO) {
                    model.images = repository.loadImages(model.id)
                    setData(model)
                    changeState(Status.DONE, ioThread = true)
                }
            } catch (e: Exception) {
                changeState(Status.ERROR, error = e)
            }
        }
    }

    private suspend fun setData(item: Breed) {
        itemBase = item
        _item.postValue(itemBase)
    }
}

@Suppress("UNCHECKED_CAST")
class BreedViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BreedViewModel(repository) as T
    }
}
