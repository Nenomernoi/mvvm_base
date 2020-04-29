package by.nrstudio.mvvm.net

import by.nrstudio.mvvm.BuildConfig
import by.nrstudio.mvvm.db.Db
import by.nrstudio.mvvm.net.response.Breed
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

	@GET("breeds")
	suspend fun getBreeds(
		@Query("limit") limit: Int,
		@Query("page") page: Int
	): Response<List<Breed>>
}

class Repository(private val api: Api, private val db: Db) : SafeApiRequest() {
	companion object {
		const val LIMIT_PAGE = 15
	}

	suspend fun loadBreeds(page: Int): MutableList<Breed> {
		var items = db.breedDao().getItems(page * Repository.LIMIT_PAGE, Repository.LIMIT_PAGE)
		if (items.isNullOrEmpty()) {
			items = apiRequest { api.getBreeds(LIMIT_PAGE, page) }.toMutableList()
			items.map {
				it.image_url = "${BuildConfig.BASE_URL}images/search?breed_id=${it.id}&format=src"
			}
			db.breedDao().insert(items)
		}
		return items
	}

	suspend fun clearData() {
		db.breedDao().deleteAll()
	}
}
