package by.nrstudio.mvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.nrstudio.mvvm.db.converter.WeightConverter
import by.nrstudio.mvvm.db.dao.BreedDao
import by.nrstudio.mvvm.db.dao.ImageDao
import by.nrstudio.mvvm.net.response.Breed
import by.nrstudio.mvvm.net.response.Image

@Database(entities = [Breed::class, Image::class], version = 3, exportSchema = false)
@TypeConverters(WeightConverter::class)
abstract class Db : RoomDatabase() {

	abstract fun breedDao(): BreedDao
    abstract fun imageDao(): ImageDao

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
