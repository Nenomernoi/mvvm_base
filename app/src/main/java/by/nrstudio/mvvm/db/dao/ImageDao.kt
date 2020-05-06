package by.nrstudio.mvvm.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import by.nrstudio.mvvm.net.response.Image

@Dao
interface ImageDao {

    companion object {
        private const val TABLE_NAME = "images"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(it: Image): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(it: List<Image>): MutableList<Long>

    @Query(value = "SELECT * FROM $TABLE_NAME  WHERE id = :parentId  ORDER BY id ASC")
    suspend fun getAll(parentId: String): MutableList<Image>

    @Query(value = "SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun getItem(id: String): Image?

    @Update
    suspend fun update(it: Image)

    @Delete
    suspend fun delete(it: Image)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()
}
