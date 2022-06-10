package org.base.main.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.db.Db
import org.base.db.dao.BreedDao
import org.base.db.dao.FavoriteDao
import org.base.db.dao.ImageDao
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

@ExperimentalCoroutinesApi
val dbModule = Kodein.Module(name = "DbModule") {
    bind<BreedDao>() with singleton { Db.breedDao(instance()) }
    bind<FavoriteDao>() with singleton { Db.favoriteDao(instance()) }
    bind<ImageDao>() with singleton { Db.imageDao(instance()) }
}
