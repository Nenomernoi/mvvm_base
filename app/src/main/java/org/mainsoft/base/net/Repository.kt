package org.mainsoft.base.net

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.kodein.di.generic.instance
import org.mainsoft.base.App
import org.mainsoft.base.BuildConfig
import org.mainsoft.base.db.Db
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.net.response.Image
import org.mainsoft.base.util.SettingPrefs
import org.mainsoft.base.util.addFavorite
import org.mainsoft.base.util.isFavorite

class Repository {

    companion object {
        private const val LIMIT = 15
    }

    private val api: Api by App.kodein.instance()
    private val setting: SettingPrefs by App.kodein.instance()
    private val db: Db by App.kodein.instance()

    suspend fun getBreeds(page: Int): MutableList<Breed> = coroutineScope {
        async {
            var items = db.breedDao().getItems(page * LIMIT, LIMIT)
            if (items.isNullOrEmpty()) {
                val buffer = api.getBreeds(LIMIT, page)
                items = buffer
                        .map { it to getImage(it.id) }
                        .map { (breed, image) ->
                            breed.copy(image_url = image)
                        }
                        .map { it to isFavorite(it.id) }
                        .map { (breed, isFavorite) ->
                            breed.copy(favorite = isFavorite)
                        }
                        .toMutableList()
                db.breedDao().insert(items)
            }
            return@async items
        }
    }.await()

    suspend fun getBreed(breedId: String): Breed = coroutineScope {
        async {
            val result = db.breedDao().getItem(breedId) ?: api.getBreed(breedId)[0].getBreed()
            result.favorite = isFavorite(result.id)
            return@async result
        }
    }.await()

    suspend fun getImages(id: String, page: Int = 0): MutableList<Image> = coroutineScope {
        async {
            var items = db.imageDao().getItems(id, page * LIMIT, LIMIT)
            if (items.isNullOrEmpty()) {
                items = api.getImages(id, page, LIMIT, "json")
                items.map {
                    it.parentId = id
                }
                db.imageDao().insert(items)
            }

            return@async items
        }
    }.await()

    suspend fun clearData() = coroutineScope {
        async {
            db.breedDao().deleteAll()
        }
    }.await()

    private suspend fun getImage(id: String): String = coroutineScope {
        async {
            "${BuildConfig.BASE_URL}images/search?breed_id=$id&format=src"
        }
    }.await()

    private suspend fun isFavorite(id: String): Boolean = coroutineScope {
        async {
            setting.isFavorite(id)
        }
    }.await()

    suspend fun addFavorite(breed: Breed): Breed = coroutineScope {
        setting.addFavorite(breed.id)
        async {
            breed.copy(favorite = !breed.favorite)
        }
    }.await()
}