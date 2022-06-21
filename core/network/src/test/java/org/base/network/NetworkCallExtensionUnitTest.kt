import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.base.functional_programming.Either
import org.base.functional_programming.Failure
import org.base.functional_programming.utils.toError
import org.base.functional_programming.utils.toSuccess
import org.base.network.AnotherDumbMiddleware
import org.base.network.DumbMiddleware
import org.base.network.models.base.ResponseError
import org.base.network.models.exception.NetworkMiddlewareFailure
import org.base.network.models.exception.ServiceBodyFailure
import org.base.network.utils.call
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class NetworkCallExtensionTest {

    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)
    private val moshi = Moshi.Builder().build()
    private val adapter: JsonAdapter<ResponseError> = moshi.adapter(ResponseError::class.java)

    @Test
    fun `when lambda returns successfully then it should emit the result as success`() {
        testScope.runTest {

            val lambdaResult = true
            val result = call(ioDispatcher = dispatcher, adapter = adapter) { lambdaResult }

            assertEquals(
                lambdaResult.toSuccess(),
                result
            )
        }
    }

    @Test
    fun `when lambda throws IOException then it should emit the result as UnknownFailure`() {
        testScope.runTest {

            val result = call(
                ioDispatcher = dispatcher,
                adapter = adapter
            ) { throw IOException("The something happened") }

            assertEquals(
                Failure.UnexpectedFailure(message = "The something happened").toError(),
                result
            )
        }
    }

    @Test
    fun `when lambda throws HttpException then it should emit the result as GenericError`() {
        val errorBody = "{\"status_message\": \"Invalid Request\",\"status_code\": 400}"
            .toResponseBody("application/json".toMediaTypeOrNull())

        testScope.runTest {
            val result = call(ioDispatcher = dispatcher, adapter = adapter) {
                throw HttpException(Response.error<Any>(400, errorBody))
            }
            assertEquals(
                ServiceBodyFailure(
                    internalCode = 400,
                    internalMessage = "Invalid Request"
                ).toError(),
                result
            )
        }
    }

    @Test
    fun `when lambda throws unknown exception then it should emit UnknownFailure`() {
        testScope.runTest {
            val result = call(ioDispatcher = dispatcher, adapter = adapter) {
                throw IllegalStateException("")
            }
            assertEquals(
                Failure.UnexpectedFailure("").toError(),
                result
            )
        }
    }

    @Test
    fun `when middleware is not valid return its failure`() {
        testScope.runTest {
            val dumbMiddleware = DumbMiddleware(
                hardCodedValidation = false,
                middlewareFailureMessage = "X"
            )
            val middlewares = listOf(
                AnotherDumbMiddleware(),
                AnotherDumbMiddleware(),
                dumbMiddleware,
                AnotherDumbMiddleware()
            )
            val result = call(
                middleWares = middlewares,
                ioDispatcher = dispatcher,
                adapter = adapter
            ) {
                10
            }
            println("dumMiddleware: ${dumbMiddleware.failure.middleWareExceptionMessage}")
            println("result: $result")

            with(result as Either.Error<NetworkMiddlewareFailure>) {
                assertEquals(
                    dumbMiddleware.failure.middleWareExceptionMessage,
                    this.error.middleWareExceptionMessage
                )
            }
        }
    }
}
