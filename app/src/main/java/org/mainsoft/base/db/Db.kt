package org.mainsoft.base.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.mainsoft.base.db.converter.WeightConverter
import org.mainsoft.base.db.dao.BreedDao
import org.mainsoft.base.net.response.Breed

@Database(entities = [Breed::class], version = 8)
@TypeConverters(WeightConverter::class)
abstract class Db : RoomDatabase() {

    abstract fun breedDao(): BreedDao

    companion object {

        @Volatile
        private var INSTANCE: Db? = null

        fun getInstance(context: Context): Db = INSTANCE ?: synchronized(this) {
            INSTANCE ?: createDatabase(context).also { INSTANCE = it }
        }

        private fun createDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext, Db::class.java, "app.db")
                        .fallbackToDestructiveMigration()
                        .build()
    }
}