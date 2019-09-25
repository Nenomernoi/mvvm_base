package org.mainsoft.base.screen.model.images

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mainsoft.base.net.Repository

object ImagesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ImagesViewModel(ImagesUseCase(Repository())) as T
}