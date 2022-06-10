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
    suspend fun insert(it: FavoriteDb): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(it: List<FavoriteDb>): MutableList<Long>

    @Query(value = "SELECT * FROM $TABLE_NAME ORDER BY id ASC")
    suspend fun getAll(): MutableList<FavoriteDb>

    @Query(value = "SELECT * FROM $TABLE_NAME ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getPage(offset: Int, limit: Int): MutableList<FavoriteDb>

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE id = :uuid")
    suspend fun getItem(uuid: Long): FavoriteDb?

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE imageId = :imageId")
    suspend fun getItem(imageId: String): FavoriteDb?

    @Update
    suspend fun update(it: FavoriteDb)

    @Delete
    suspend fun delete(it: FavoriteDb)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

    @Query("DELETE FROM $TABLE_NAME WHERE isFavorite = 0")
    suspend fun deleteUnFavoriteAll()
}
