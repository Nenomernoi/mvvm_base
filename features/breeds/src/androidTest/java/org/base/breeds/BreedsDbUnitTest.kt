package org.base.breeds

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.base.breeds.data.BreedsDbData
import org.base.breeds_data.db.data_source.BreedsDbRepositoryImpl
import org.base.breeds_data.db.repository.BreedsDbRepository
import org.base.common.models.domain.Breed
import org.base.common.models.mapper.BreedMapper
import org.base.common.models.mapper.BreedMapperImpl
import org.base.db.Db
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BreedsDbUnitTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val mapper: BreedMapper = BreedMapperImpl(
        defaultDispatcher = testDispatcher
    )

    private lateinit var base: Db
    private lateinit var db: BreedsDbRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        base = Room.inMemoryDatabaseBuilder(context, Db::class.java).build()
        db = BreedsDbRepositoryImpl(
            ioDispatcher = testDispatcher,
            daoBreeds = base.breedDao()
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        base.close()
    }

    @Test
    fun stage1_writeBreedsFromRemote() {
        val domainBreeds: List<Breed> = BreedsDbData.provideRemoteBreedsFromAssets()
        testScope.runTest {
            val dbBreeds = mapper.mapDomainListToDb(domainBreeds)
            val result = db.saveAllBreeds(dbBreeds)
            Assert.assertTrue(
                "No save all items",
                result.isSuccess
            )
        }
    }

    @Test
    fun stage2_readEarlySaveBreeds() {
        val domainBreeds: List<Breed> = BreedsDbData.provideRemoteBreedsFromAssets()
        testScope.runTest {
            val dbBreeds = mapper.mapDomainListToDb(domainBreeds)
            db.saveAllBreeds(dbBreeds)
            val result = db.getPageBreeds(page = 0, limit = 15)
            Assert.assertTrue(
                "Not read items",
                result.isSuccess
            )
            val readBreeds = result.getSuccessOrNull() ?: listOf()
            Assert.assertEquals(
                "Not all items Read",
                readBreeds.size,
                dbBreeds.size
            )
            val correct = dbBreeds.map { db -> readBreeds.any { it.uuid == db.uuid } }.filter { it }
            Assert.assertEquals(
                "Not all items Read",
                correct.size,
                dbBreeds.size
            )
        }
    }
}
