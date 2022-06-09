package by.nrstudio.common.models.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BreedUi(
    val id: String,

    val name: String,
    val description: String,
    val image: String,

    val countryFlag: String,
) : Parcelable
