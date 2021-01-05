package org.mainsoft.base.db.dao

import androidx.room.*
import org.mainsoft.base.net.response.Image

@Dao
interface ImageDao {

    companion object {
        private const val TABLE_NAME = "images"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(it: Image): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(it: List<Image>): MutableList<Long>

    @Query(value = "SELECT * FROM $TABLE_NAME  WHERE parentId = :parentId  ORDER BY id ASC")
    suspend fun getAll(parentId: String): MutableList<Image>

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun getItem(id: String): Image?

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE parentId = :parentId ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getItems(parentId: String, offset: Int, limit: Int): MutableList<Image>

    @Update
    suspend fun update(it: Image)

    @Delete
    suspend fun delete(it: Image)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()
}
