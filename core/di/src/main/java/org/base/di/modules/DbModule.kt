package org.base.di.modules

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.db.Db
import org.base.db.dao.BreedDao
import org.base.db.dao.FavoriteDao
import org.base.db.dao.ImageDao
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

@ExperimentalCoroutinesApi
val dbModule = DI.Module(name = "DbModule") {
    bind<BreedDao>() with singleton { Db.breedDao(instance()) }
    bind<FavoriteDao>() with singleton { Db.favoriteDao(instance()) }
    bind<ImageDao>() with singleton { Db.imageDao(instance()) }
}
