import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.base.functional_programming.Either
import org.base.functional_programming.Failure
import org.base.functional_programming.utils.toError
import org.base.functional_programming.utils.toSuccess
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class EitherUnitTest {

    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testCoroutineDispatcher)

    @Test
    fun `assert coMapSuccess method will return a Success struct and not a Failure`() {
        testScope.runTest {
            val myAge = getAgeService().coMapSuccess { age -> age  }
            Assert.assertTrue("My Age is not success.", myAge.isSuccess)

            myAge.coMapSuccess { resultAge ->
                Assert.assertEquals(34, resultAge)
            }
        }
    }

    @Test
    fun `assert coMapSuccess method will pass the failure if is not a Success`() {
        testScope.runTest {
            val someAge = someFailure().coMapSuccess { ageIfSuccess -> ageIfSuccess + 1 }
            Assert.assertTrue("My age is a success after all", someAge.isError)
            Assert.assertFalse(someAge.isSuccess)

            with(someAge as Either.Error<Failure>) {
                with(this.error as Failure.UnexpectedFailure) {
                    assertEquals(message, "ups!")
                }
            }
        }
    }

    private fun getAgeService(): Either<Failure, Int> =
        (2022.minus(1988)).toSuccess()

    private fun someFailure(): Either<Failure, Int> =
        Failure.UnexpectedFailure("ups!").toError()
}
