package org.base.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.base.db.model.BreedDb

@Dao
interface BreedDao {

    companion object {
        const val TABLE_NAME = "breeds"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(it: BreedDb): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(it: List<BreedDb>): MutableList<Long>

    @Query(value = "SELECT * FROM $TABLE_NAME ORDER BY name ASC")
    suspend fun getAll(): MutableList<BreedDb>

    @Query(value = "SELECT * FROM $TABLE_NAME ORDER BY name ASC LIMIT :limit OFFSET :offset")
    suspend fun getPage(offset: Int, limit: Int): MutableList<BreedDb>

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE uuid = :uuid")
    suspend fun getItem(uuid: String): BreedDb?

    @Update
    suspend fun update(it: BreedDb)

    @Delete
    suspend fun delete(it: BreedDb)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()
}
