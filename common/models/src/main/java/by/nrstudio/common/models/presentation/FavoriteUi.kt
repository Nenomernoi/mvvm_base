package by.nrstudio.common.models.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteUi(
    val id: Long,
    val imageId: String,
    val image: String,

    val isFavorite: Boolean = false,
) : Parcelable
