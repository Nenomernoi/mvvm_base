package org.mainsoft.base.net

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.kodein.di.generic.instance
import org.mainsoft.base.App
import org.mainsoft.base.BuildConfig
import org.mainsoft.base.db.Db
import org.mainsoft.base.net.response.Breed

class Repository {

    companion object {
        private const val LIMIT = 15
    }

    private val api: Api by App.kodein.instance()
    private val db: Db by App.kodein.instance()

    suspend fun getBreeds(page: Int): MutableList<Breed> = coroutineScope {
        async {
            var items = db.breedDao().getBreeds(page * LIMIT, LIMIT)
            if (items.isNullOrEmpty()) {
                val buffer = api.getBreeds(LIMIT, page)
                items = buffer
                        .map { it to getImage(it.id) }
                        .map { (breed, image) ->
                            breed.copy(image_url = image)
                        }.toMutableList()
                db.breedDao().insert(items)
            }
            return@async items
        }
    }.await()


    suspend fun getBreed(breedId: String): Breed = coroutineScope {
        async {
            return@async db.breedDao().getItem(breedId) ?: api.getBreed(breedId)[0].getBreed()
        }
    }.await()

    suspend fun clearData() = coroutineScope {
        async {
            db.breedDao().deleteAll()
        }
    }.await()

    suspend fun getImage(id: String): String = coroutineScope {
        async {
            "${BuildConfig.BASE_URL}images/search?breed_id=$id&format=src"
        }
    }.await()

    suspend fun showFull(user: Breed): Breed = coroutineScope {
        async {
            user.copy(favorite = !user.favorite)
        }
    }.await()
}