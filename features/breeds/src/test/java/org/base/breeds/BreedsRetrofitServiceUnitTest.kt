package org.base.breeds

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.base.breeds.data.BreedsData
import org.base.breeds_data.data_source.remote.retrofit_service.BreedsService
import org.base.test_shared.file.FileReaderUtil
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class BreedsRetrofitServiceUnitTest {

    private val mockWebServer = MockWebServer()
    private lateinit var breedsService: BreedsService

    @Before
    fun setUp() {
        mockWebServer.start()
        mockWebServer.dispatcher = setUpMockWebServerDispatcher()
        setUpActorsRetrofitService()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Assert get actors remote response structure match JSON Server response`() = runBlocking {
        val breeds = breedsService.getBreeds(
            limit = 15,
            page = 0
        )
        Assert.assertEquals(
            "Breeds size does not match the one provided in resources.",
            BreedsData.provideRemoteBreedsFromAssets(0).size,
            breeds.size
        )
    }

    private fun setUpActorsRetrofitService() {
        breedsService = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(BreedsService::class.java)
    }

    private fun setUpMockWebServerDispatcher(): Dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            println("BASE_URL${request.path}")
            return when (request.path) {
                "/breeds?limit=15&page=0" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(FileReaderUtil.kotlinReadFileWithNewLineFromResources("breeds_0.json"))
                }
                else -> MockResponse().setResponseCode(404)
            }
        }
    }
}
