package org.base.favorites.presentation.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.base.common.models.presentation.FavoriteUi
import org.base.favorites.R
import org.base.favorites.databinding.FragmentFavoritesBinding
import org.base.favorites.presentation.ui.favorites.adapter.FavoriteAdapter
import org.base.favorites.presentation.ui.favorites.intent.FavoritesIntent
import org.base.mvi.Status
import org.base.ui_components.adapter.BaseItemListener
import org.base.ui_components.adapter.listeners.flowEndless
import org.base.ui_components.adapter.listeners.flowRefresh
import org.base.ui_components.adapter.managers.BaseGridLayoutManager
import org.base.ui_components.ui.BaseListFragment
import org.base.ui_components.ui.listener.flowClick
import org.kodein.di.instance

@FlowPreview
@ExperimentalCoroutinesApi
class FavoritesFragment : BaseListFragment<FavoriteUi, FavoritesIntent, FavoritesUiState>(R.layout.fragment_favorites) {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoritesViewModel by instance()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false).apply {
            this@FavoritesFragment.rvMain = rvMain
            this@FavoritesFragment.pbLoad = pbLoad
            this@FavoritesFragment.tvError = tvError
        }
        return binding
    }

    override fun initData() {
        super.initData()
        collectUiState()
    }

    override fun initRclView() {
        binding.rvMain.layoutManager = BaseGridLayoutManager(requireActivity(), 2, LinearLayoutManager.VERTICAL, false)
        addBottomSpaceAdapter()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                render(state)
            }
        }
    }

    override fun initAdapter() {
        adapter = FavoriteAdapter(object : BaseItemListener {
            override fun onItem(position: Int, action: Int) {
                when (action) {
                    R.drawable.ic_favorite_border_color -> {
                        viewModel.processIntents(FavoritesIntent.RemoveFromFavoritesIntent, position)
                    }
                    R.drawable.ic_favorite_border -> {
                        viewModel.processIntents(FavoritesIntent.AddToFavoritesIntent, position)
                    }
                    else -> {
                        // TODO select item
                    }
                }
            }
        })
        binding.rvMain.setHasFixedSize(true)
    }

    override fun initListeners() {
        intents()
            .onEach { intent ->
                viewModel.processIntents(
                    intent,
                    when (intent) {
                        is FavoritesIntent.LoadNextIntent -> intent.page
                        else -> Unit
                    }
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun intents(): Flow<FavoritesIntent> {
        val flowIntents = listOf(
            binding.sRefresh.flowRefresh().map { FavoritesIntent.SwipeOnRefreshIntent },
            binding.rvMain.flowEndless().map { FavoritesIntent.LoadNextIntent(it) },
            binding.tvError.flowClick().map { FavoritesIntent.LoadLastIntent }
        )
        return flowIntents.asFlow().flattenMerge(flowIntents.size)
    }

    override fun render(state: FavoritesUiState) {
        with(state) {
            showHideProgress(state.status == Status.LOADING)
            setData(state.data)
            showError(error, state.status == Status.ERROR)
        }
    }
}
