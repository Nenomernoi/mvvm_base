package org.mainsoft.base.db.dao

import androidx.room.*
import org.mainsoft.base.net.response.Breed

@Dao
interface BreedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(it: Breed): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(it: List<Breed>): MutableList<Long>

    @Query(value = "SELECT * FROM breeds ORDER BY name ASC")
    suspend fun getAll(): MutableList<Breed>

    @Query(value = "SELECT * FROM breeds ORDER BY name ASC LIMIT :limit OFFSET :offset")
    suspend fun getBreeds(offset: Int, limit: Int): MutableList<Breed>

    @Update
    suspend fun update(it: Breed)

    @Delete
    suspend fun delete(it: Breed)

    @Query("DELETE FROM breeds")
    suspend fun deleteAll()
}
