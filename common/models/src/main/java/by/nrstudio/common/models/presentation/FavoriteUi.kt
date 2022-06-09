package by.nrstudio.common.models.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteUi(
    var id: Long,
    val imageId: String,
    val image: String,

    var isFavorite: Boolean = false,
) : Parcelable
