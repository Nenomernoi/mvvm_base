package by.nrstudio.basemodulesmvi.di

import by.nrstudio.common.models.mapper.BreedMapper
import by.nrstudio.common.models.mapper.BreedMapperImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val commonModelsModule = Kodein.Module(name = "CommonModelsModule") {
    bind<BreedMapper>() with singleton { BreedMapperImpl(defaultDispatcher = instance(arg = "defaultDispatcher")) }
}
