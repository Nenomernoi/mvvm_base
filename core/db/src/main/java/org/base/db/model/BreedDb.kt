package org.base.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.base.db.dao.BreedDao

@Entity(tableName = BreedDao.TABLE_NAME)
data class BreedDb(

    @PrimaryKey(autoGenerate = false)
    val uuid: String,

    val name: String,
    val description: String,
    val image: String,

    val weight: String,
    val lifeSpan: String,

    val countryCode: String,
    val origin: String,
)
