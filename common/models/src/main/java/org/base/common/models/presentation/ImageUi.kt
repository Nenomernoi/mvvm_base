package org.base.common.models.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageUi(
    val id: String,
    val url: String,

    var idFavorite: Long = 0
) : Parcelable
