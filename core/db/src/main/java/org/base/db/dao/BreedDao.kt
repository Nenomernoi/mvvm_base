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
    fun insert(it: BreedDb): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(it: List<BreedDb>): MutableList<Long>

    @Query(value = "SELECT * FROM $TABLE_NAME ORDER BY name ASC")
    fun getAll(): MutableList<BreedDb>

    @Query(value = "SELECT * FROM $TABLE_NAME ORDER BY name ASC LIMIT :limit OFFSET :offset")
    fun getPage(offset: Int, limit: Int): MutableList<BreedDb>

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE uuid = :uuid")
    fun getItem(uuid: String): BreedDb?

    @Update
    fun update(it: BreedDb)

    @Delete
    fun delete(it: BreedDb)

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAll()
}
