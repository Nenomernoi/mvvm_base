package org.base.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.base.db.dao.BreedDao
import org.base.db.dao.FavoriteDao
import org.base.db.dao.ImageDao
import org.base.db.model.BreedDb
import org.base.db.model.FavoriteDb
import org.base.db.model.ImageDb

@Database(entities = [ImageDb::class, BreedDb::class, FavoriteDb::class], version = 1)
abstract class Db : RoomDatabase() {

    abstract fun imageDao(): ImageDao
    abstract fun breedDao(): BreedDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {

        @Volatile
        private var INSTANCE: Db? = null

        private fun getInstance(context: Context): Db = INSTANCE ?: synchronized(this) {
            INSTANCE ?: createDatabase(context).also { INSTANCE = it }
        }

        fun imageDao(context: Context) = getInstance(context).imageDao()
        fun breedDao(context: Context) = getInstance(context).breedDao()
        fun favoriteDao(context: Context) = getInstance(context).favoriteDao()

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, Db::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
