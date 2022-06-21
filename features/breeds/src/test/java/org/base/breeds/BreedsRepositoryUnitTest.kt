package org.base.breeds

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.base.breeds.data.BreedsData
import org.base.breeds_data.data.data_source.BreedsRepositoryImpl
import org.base.breeds_data.data.repository.BreedsRemoteDataSource
import org.base.breeds_data.data_source.remote.BreedsRemoteDataSourceImpl
import org.base.breeds_data.data_source.remote.retrofit_service.BreedsService
import org.base.breeds_data.domain.BreedsRepository
import org.base.common.models.data.BreedResponse
import org.base.common.models.mapper.BreedMapper
import org.base.common.models.mapper.BreedMapperImpl
import org.base.network.middleware.provider.MiddlewareProvider
import org.base.network.models.exception.NetworkMiddlewareFailure
import org.base.network.models.exception.ServiceBodyFailure
import org.base.test_shared.either.getDataWhenResultIsFailureOrThrowException
import org.base.test_shared.either.getDataWhenResultIsSuccessOrThrowException
import org.base.test_shared.middleware.DefaultTestNetworkMiddleware
import org.base.test_shared.network.DefaultRemoteConfig
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class BreedsRepositoryUnitTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val remoteErrorAdapter = DefaultRemoteConfig.provideRemoteErrorAdapter()

    private val breedsService = mockk<BreedsService>()
    private val middlewareProvider = mockk<MiddlewareProvider>()

    private val remoteDataSource: BreedsRemoteDataSource = BreedsRemoteDataSourceImpl(
        middlewareProvider = middlewareProvider,
        ioDispatcher = testDispatcher,
        adapter = remoteErrorAdapter,
        serviceBreeds = breedsService
    )

    private val mapper: BreedMapper = BreedMapperImpl(
        defaultDispatcher = testDispatcher
    )

    private val repository: BreedsRepository = BreedsRepositoryImpl(
        remoteDataSourceBreeds = remoteDataSource,
        mapperBreeds = mapper
    )

    private fun `Assert repository return breeds when remote service works as expected`(page: Int) {
        val remoteBreeds: List<BreedResponse> = BreedsData.provideRemoteBreedsFromAssets(page = page)

        every { middlewareProvider.getAll() } returns listOf(DefaultTestNetworkMiddleware(isMiddlewareValid = true))

        coEvery {
            breedsService.getBreeds(limit = any(), page = any())
        } returns remoteBreeds

        testScope.runTest {
            repository.getBreeds(page = page, limit = 15).getDataWhenResultIsSuccessOrThrowException { domainBreeds ->
                assertEquals(
                    "Remote breeds size does not match the domain breeds size",
                    remoteBreeds.size,
                    domainBreeds.size
                )
                val remoteBreed = remoteBreeds.first()
                val domainBreed = domainBreeds.first()

                assertEquals(
                    "Remote breed 'id' is not the same as the Domain breed 'id'",
                    remoteBreed.id,
                    domainBreed.id
                )
                assertEquals(
                    "Remote breed 'name' is not the same as the Domain breed 'name'",
                    remoteBreed.name,
                    domainBreed.name
                )
                assertEquals(
                    "Remote breed 'description' is not the same as the Domain breed 'description'",
                    remoteBreed.description,
                    domainBreed.description
                )
                assertEquals(
                    "Remote breed 'image' is not the same as the Domain breed 'image'",
                    remoteBreed.image?.url,
                    domainBreed.image
                )
                assertEquals(
                    "Remote breed 'lifeSpan' is not the same as the Domain breed 'lifeSpan'",
                    remoteBreed.lifeSpan,
                    domainBreed.lifeSpan
                )
                assertEquals(
                    "Remote breed 'origin' is not the same as the Domain breed 'origin'",
                    remoteBreed.origin,
                    domainBreed.origin
                )
                assertEquals(
                    "Remote breed 'countryCode' is not the same as the Domain breed 'countryCode'",
                    remoteBreed.countryCode,
                    domainBreed.countryCode
                )
                assertEquals(
                    "Remote breed 'weight' is not the same as the Domain breed 'weight'",
                    remoteBreed.weight.metric,
                    domainBreed.weight
                )
            }
        }
    }

    @Test
    fun `Assert repository return breeds (page 0) when remote service works as expected`() {
        `Assert repository return breeds when remote service works as expected`(0)
    }

    @Test
    fun `Assert repository return breeds (page 1) when remote service works as expected`() {
        `Assert repository return breeds when remote service works as expected`(1)
    }

    @Test
    fun `Assert repository return network middleware failure message properly`() {

        every { middlewareProvider.getAll() } returns listOf(
            DefaultTestNetworkMiddleware(
                isMiddlewareValid = false,
                failureMessage = "No network detected"
            )
        )
        testScope.runTest {
            repository.getBreeds(limit = 15, page = 0).getDataWhenResultIsFailureOrThrowException { failure ->
                Assert.assertTrue(
                    "Failure returned by repository is not of NetworkMiddlewareFailure",
                    failure is NetworkMiddlewareFailure
                )
                with(failure as NetworkMiddlewareFailure) {
                    assertEquals(
                        "No network detected",
                        middleWareExceptionMessage
                    )
                }
            }
            coVerify(exactly = 0) { breedsService.getBreeds(any(), any()) }
        }
    }

    @Test
    fun `Assert repository return network service call exception properly`() {
        val errorBody = "{\"status_message\": \"Invalid Request\",\"status_code\": 400}"
            .toResponseBody("application/json".toMediaTypeOrNull())

        every { middlewareProvider.getAll() } returns listOf(
            DefaultTestNetworkMiddleware(
                isMiddlewareValid = true
            )
        )

        coEvery {
            breedsService.getBreeds(any(), any())
        } throws HttpException(Response.error<Any>(400, errorBody))

        testScope.runTest {
            repository.getBreeds(limit = 15, page = 0)
                .getDataWhenResultIsFailureOrThrowException { failure ->
                    assertEquals(
                        ServiceBodyFailure(
                            internalCode = 400,
                            internalMessage = "Invalid Request"
                        ),
                        failure
                    )
                }
        }
    }
}
