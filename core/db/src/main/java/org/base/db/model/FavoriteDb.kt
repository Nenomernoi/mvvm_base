package org.base.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.base.db.dao.FavoriteDao

@Entity(tableName = FavoriteDao.TABLE_NAME)
data class FavoriteDb(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val imageId: String,
    val image: String,

    var isFavorite: Boolean = false,
)
