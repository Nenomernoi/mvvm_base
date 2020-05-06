package by.nrstudio.mvvm.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import by.nrstudio.mvvm.net.Repository
import by.nrstudio.mvvm.net.response.Breed

@Dao
interface BreedDao {

	companion object {
		private const val TABLE_NAME = "breeds"
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(it: Breed): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(it: List<Breed>): MutableList<Long>

	@Query(value = "SELECT * FROM $TABLE_NAME ORDER BY name ASC")
	suspend fun getAll(): MutableList<Breed>

	@Query(value = "SELECT * FROM $TABLE_NAME WHERE id = :id")
	suspend fun getItem(id: String): Breed?

	@Query(value = "SELECT * FROM $TABLE_NAME ORDER BY name ASC LIMIT :limit OFFSET :offset")
	suspend fun getItems(offset: Int, limit: Int = Repository.LIMIT_PAGE): MutableList<Breed>

	@Update
	suspend fun update(it: Breed)

	@Delete
	suspend fun delete(it: Breed)

	@Query("DELETE FROM $TABLE_NAME")
	suspend fun deleteAll()
}
