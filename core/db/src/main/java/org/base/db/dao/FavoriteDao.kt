package org.base.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.base.db.model.FavoriteDb

@Dao
interface FavoriteDao {

    companion object {
        const val TABLE_NAME = "favorites"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(it: FavoriteDb): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(it: List<FavoriteDb>): MutableList<Long>

    @Query(value = "SELECT * FROM $TABLE_NAME ORDER BY uuid ASC")
    fun getAll(): MutableList<FavoriteDb>

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE imageId IN(:imageIds) ORDER BY uuid ASC")
    fun getAllFromIds(imageIds: List<String>): MutableList<FavoriteDb>

    @Query(value = "SELECT * FROM $TABLE_NAME ORDER BY uuid ASC LIMIT :limit OFFSET :offset")
    fun getPage(offset: Int, limit: Int): MutableList<FavoriteDb>

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE uuid = :uuid")
    fun getItem(uuid: Long): FavoriteDb?

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE imageId = :imageId")
    fun getItem(imageId: String): FavoriteDb?

    @Update
    fun update(it: FavoriteDb)

    @Delete
    fun delete(it: FavoriteDb)

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAll()

    @Query("DELETE FROM $TABLE_NAME WHERE isFavorite = 0")
    fun deleteUnFavoriteAll()
}
