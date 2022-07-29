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
import org.base.breeds.data.BreedsDbData
import org.base.breeds.presentation.ui.breeds.BreedsFragment
import org.base.breeds.presentation.ui.breeds.BreedsUiState
import org.base.breeds.presentation.ui.breeds.BreedsViewModel
import org.base.common.models.presentation.BreedUi
import org.base.mvi.Status
import org.base.ui_components.BaseApp
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import java.util.concurrent.TimeUnit
import org.base.breed.di.featureBreedModule

@OptIn(FlowPreview::class)
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BreedsFragmentInstrumentedTest {

    private lateinit var scenario: FragmentScenario<BreedsFragment>
    private val breedsViewModel: BreedsViewModel = mockk(relaxed = true)

    @Before
    fun setUp() {
/*
        BaseApp.di = DI.lazy {
            import(
                featureBreedModule.apply {
                    import(
                        DI.Module(name = "BreedsViewModel") {
                            bind<BreedsViewModel>() with singleton { breedsViewModel }
                        }
                    )
                },
                allowOverride = true
            )
        }
*/
        scenario = launchFragmentInContainer(themeResId = androidx.appcompat.R.style.Base_Theme_AppCompat_Light)
        scenario.moveToState(newState = Lifecycle.State.STARTED)
    }

    @Test
    fun stage1_assert_Breeds_List_Fragment_Render_The_UI_According_The_Loading_State() {
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

        TimeUnit.SECONDS.sleep(1)
    }

    @Test
    fun stage2_assert_Breeds_List_Fragment_Render_The_UI_According_The_Loaded_State() {

        val uiBreeds = BreedsDbData.provideUiBreedsFromAssets()

        scenario.onFragment { fragment ->
            fragment.render(
                state = BreedsUiState(
                    status = Status.DONE,
                    data = mutableListOf<BreedUi>().apply {
                        addAll(uiBreeds)
                    },
                    error = null,
                    page = 0
                )
            )
        }

        TimeUnit.SECONDS.sleep(2)

        Espresso.onView(withId(R.id.tvTitle))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.pbLoad))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        Espresso.onView(withId(R.id.tvError))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        Espresso.onView(withId(R.id.rvMain))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        TimeUnit.SECONDS.sleep(3)
    }
}
