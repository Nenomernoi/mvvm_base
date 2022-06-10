package org.base.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.base.db.dao.ImageDao

@Entity(tableName = ImageDao.TABLE_NAME)
data class ImageDb(

    @PrimaryKey(autoGenerate = false)
    val uuid: String,

    val url: String,

    val idBreed: String,

    val idFavorite: Long = 0,
)
