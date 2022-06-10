package org.base.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.base.db.model.ImageDb

@Dao
interface ImageDao {

    companion object {
        const val TABLE_NAME = "images"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(it: ImageDb): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(it: List<ImageDb>): MutableList<Long>

    @Query(value = "SELECT * FROM $TABLE_NAME ORDER BY uuid ASC")
    fun getAll(): MutableList<ImageDb>

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE idBreed = :idBreed ORDER BY uuid ASC LIMIT :limit OFFSET :offset")
    fun getPage(idBreed: String, offset: Int, limit: Int): MutableList<ImageDb>

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE uuid = :uuid")
    fun getItem(uuid: String): ImageDb

    @Update
    fun update(it: ImageDb)

    @Delete
    fun delete(it: ImageDb)

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAll()
}
