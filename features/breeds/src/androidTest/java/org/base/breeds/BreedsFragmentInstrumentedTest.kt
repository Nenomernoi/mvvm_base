package org.base.breeds

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.base.breeds.presentation.ui.breeds.BreedsFragment
import org.base.breeds.presentation.ui.breeds.BreedsUiState
import org.base.breeds.presentation.ui.breeds.BreedsViewModel
import org.base.mvi.Status
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(FlowPreview::class)
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class BreedsFragmentInstrumentedTest {

    private lateinit var scenario: FragmentScenario<BreedsFragment>
    private val breedsViewModel: BreedsViewModel = mockk(relaxed = true)

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer()
        scenario.moveToState(newState = Lifecycle.State.STARTED)
    }

    @Test
    fun assert_Breeds_List_Fragment_Render_The_UI_According_The_Loading_State() {
        scenario.onFragment { fragment ->
            fragment.render(
                state = BreedsUiState(
                    status = Status.LOADING,
                    data = mutableListOf(),
                    error = null,
                    page = 0
                )
            )
        }
        Espresso.onView(withId(R.id.tvTitle))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.pbLoad))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.tvError))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        Espresso.onView(withId(R.id.rvMain))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
}
