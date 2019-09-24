package org.mainsoft.base.net.response

data class BreedImage(
        val id: String,
        val url: String,
        val width: Int,
        val height: Int,
        val breeds: MutableList<Breed> = mutableListOf()
) {
    fun getBreed() : Breed {
        val res = breeds[0]
        res.image_url = url
        res.image_id = id
        return  res
    }
}