package org.base.home.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.base.breeds.presentation.ui.breeds.BreedsFragment
import org.base.favorites.presentation.ui.favorites.FavoritesFragment
import org.base.home.R
import org.base.home.databinding.FragmentHomeBinding
import org.base.ui_components.ui.BaseEmptyFragment

@FlowPreview
@ExperimentalCoroutinesApi
class HomeContainerFragment : BaseEmptyFragment(R.layout.fragment_home) {

    private val breedsFragment: BreedsFragment by lazy { BreedsFragment() }
    private val favoritesFragment: FavoritesFragment by lazy { FavoritesFragment() }
    private lateinit var activeFragment: Fragment

    private lateinit var binding: FragmentHomeBinding

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding
    }

    override fun initListeners() {
        binding.bottomNavHome.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_feature_movies -> {
                    childFragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(breedsFragment)
                        .commit()
                    activeFragment = breedsFragment
                    return@setOnItemSelectedListener true
                }
                R.id.nav_feature_actors -> {
                    childFragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(favoritesFragment)
                        .commit()
                    activeFragment = favoritesFragment
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }
    }

    override fun initData() {
        if (childFragmentManager.fragments.isEmpty()) {
            activeFragment = breedsFragment
            childFragmentManager.beginTransaction()
                .add(R.id.homeContainerView, favoritesFragment, "FavoritesFragment")
                .hide(favoritesFragment)
                .add(R.id.homeContainerView, breedsFragment, "BreedsFragment")
                .commit()
        }
    }
}
