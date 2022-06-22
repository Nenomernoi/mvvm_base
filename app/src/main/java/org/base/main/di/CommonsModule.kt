package org.base.main.di

import org.base.common.models.mapper.BreedMapper
import org.base.common.models.mapper.BreedMapperImpl
import org.base.common.models.mapper.FavoriteMapper
import org.base.common.models.mapper.FavoriteMapperImpl
import org.base.common.models.mapper.ImageMapper
import org.base.common.models.mapper.ImageMapperImpl
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val commonModelsModule = DI.Module(name = "CommonModelsModule") {
    bind<BreedMapper>() with singleton { BreedMapperImpl(defaultDispatcher = instance(arg = "defaultDispatcher")) }
    bind<FavoriteMapper>() with singleton { FavoriteMapperImpl(defaultDispatcher = instance(arg = "defaultDispatcher")) }
    bind<ImageMapper>() with singleton { ImageMapperImpl(defaultDispatcher = instance(arg = "defaultDispatcher")) }
}
