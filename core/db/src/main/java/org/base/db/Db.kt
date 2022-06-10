package org.base.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.base.db.dao.BreedDao
import org.base.db.dao.FavoriteDao
import org.base.db.model.BreedDb
import org.base.db.model.FavoriteDb

@Database(entities = [BreedDb::class, FavoriteDb::class], version = 3)
abstract class Db : RoomDatabase() {

    abstract fun breedDao(): BreedDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {

        @Volatile
        private var INSTANCE: Db? = null

        private fun getInstance(context: Context): Db = INSTANCE ?: synchronized(this) {
            INSTANCE ?: createDatabase(context).also { INSTANCE = it }
        }

        fun breedDao(context: Context) = getInstance(context).breedDao()
        fun favoriteDao(context: Context) = getInstance(context).favoriteDao()

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, Db::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
