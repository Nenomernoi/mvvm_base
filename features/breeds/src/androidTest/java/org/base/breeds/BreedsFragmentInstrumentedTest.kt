package org.base.breeds

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.breeds.presentation.ui.breeds.BreedsFragment
import org.base.breeds.presentation.ui.breeds.BreedsViewModel
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class BreedsFragmentInstrumentedTest {

    private lateinit var scenario: FragmentScenario<BreedsFragment>
    private val actorsViewModel: BreedsViewModel = mockk(relaxed = true)

    @Before
    fun setUp() {

        scenario = launchFragmentInContainer(themeResId = R.style.Base_DayNight)
        scenario.moveToState(newState = Lifecycle.State.STARTED)
    }
}