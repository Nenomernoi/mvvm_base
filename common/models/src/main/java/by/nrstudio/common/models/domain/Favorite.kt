package by.nrstudio.common.models.domain

data class Favorite(
    val id: Long,
    val imageId: String,
    val image: String,

    val isFavorite: Boolean = false,
)
