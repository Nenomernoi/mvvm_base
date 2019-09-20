package org.mainsoft.base.screen.model.breed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mainsoft.base.net.Repository

object BreedViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            BreedViewModel(BreedUseCase(Repository())) as T
}