package org.mainsoft.base.screen.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mainsoft.base.net.Repository

object BreedsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            BreedsViewModel(BreedsUseCase(Repository())) as T
}