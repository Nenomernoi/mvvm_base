package by.nrstudio.mvvm.ui.viewmodel.base

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.nrstudio.mvvm.ui.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

	private val _status = MutableLiveData<Status>()
	val status: LiveData<Status>
		get() = _status

	protected fun changeState(state: Status, ioThread: Boolean = false, error: Exception? = null) {
		if (ioThread) {
			_status.postValue(state)
		} else {
			_status.value = state
		}

		error?.let { e ->
			Log.e(javaClass.simpleName, e.message, e)
		}
	}

	fun isError() = if (status.value == Status.ERROR) View.VISIBLE else View.GONE
}

open class BaseListViewModel<T> : BaseViewModel() {

	private var itemsList: MutableList<T> = mutableListOf()
	private val _items = MutableLiveData<MutableList<T>>()
	val items: LiveData<MutableList<T>>
		get() = _items

	protected open suspend fun setData(list: MutableList<T>) {
		itemsList.addAll(list)
		_items.postValue(itemsList)
	}

	fun isEmptyData() = itemsList.isNullOrEmpty()

	fun clearData() {
		viewModelScope.launch(Dispatchers.Main) {
			itemsList = mutableListOf()
			_items.value = itemsList
			launch(Dispatchers.IO) {
				baseClearData()
			}
		}
	}

	protected open suspend fun baseClearData() {
		//
	}

	open fun onLoadNext(page: Int) {
		//
	}
}
