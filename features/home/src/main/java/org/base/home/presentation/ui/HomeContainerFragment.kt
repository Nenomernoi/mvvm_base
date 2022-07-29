package org.base.home.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.base.breeds.presentation.ui.breeds.BreedsFragment
import org.base.favorites.presentation.ui.favorites.FavoritesFragment
import org.base.home.R
import org.base.home.databinding.FragmentHomeBinding

@FlowPreview
@ExperimentalCoroutinesApi
class HomeContainerFragment : Fragment(R.layout.fragment_home) {

    companion object {
        private const val POSITION_NAV = "positionOn"
    }

    private val breedsFragment: BreedsFragment by lazy { BreedsFragment() }
    private val favoritesFragment: FavoritesFragment by lazy { FavoritesFragment() }
    private lateinit var activeFragment: Fragment

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        activeFragment = when (savedInstanceState?.getString(POSITION_NAV)) {
            BreedsFragment::class.simpleName -> breedsFragment
            FavoritesFragment::class.simpleName -> favoritesFragment
            else -> breedsFragment
        }
        openFirstScreen()
        super.onViewStateRestored(savedInstanceState)
    }

    private fun initListeners() {
        binding.bottomNavHome.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_feature_movies -> {
                    loadScreen(breedsFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_feature_actors -> {
                    loadScreen(favoritesFragment)
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }
    }

    private fun loadScreen(newFragment: Fragment) {
        childFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(newFragment)
            .commit()
        activeFragment = newFragment
    }

    private fun openFirstScreen() {
        val hideFragment = when (activeFragment) {
            is FavoritesFragment -> breedsFragment
            is BreedsFragment -> favoritesFragment
            else -> favoritesFragment
        }
        childFragmentManager.beginTransaction()
            .add(R.id.homeContainerView, hideFragment, hideFragment::class.simpleName)
            .hide(hideFragment)
            .add(R.id.homeContainerView, activeFragment, activeFragment::class.simpleName)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(POSITION_NAV, activeFragment::class.simpleName)
        super.onSaveInstanceState(outState)
    }
}
