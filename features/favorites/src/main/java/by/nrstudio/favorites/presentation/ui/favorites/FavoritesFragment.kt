package by.nrstudio.favorites.presentation.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import by.nrstudio.common.models.presentation.FavoriteUi
import by.nrstudio.favorites.R
import by.nrstudio.favorites.databinding.FragmentFavoritesBinding
import by.nrstudio.favorites.presentation.ui.favorites.adapter.FavoriteAdapter
import by.nrstudio.favorites.presentation.ui.favorites.intent.FavoritesIntent
import by.nrstudio.mvi.Status
import by.nrstudio.ui_components.adapter.BaseItemListener
import by.nrstudio.ui_components.adapter.listeners.flowEndless
import by.nrstudio.ui_components.adapter.listeners.flowRefresh
import by.nrstudio.ui_components.ui.BaseListFragment
import by.nrstudio.ui_components.ui.listener.flowClick
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

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
                        viewModel.processIntents(FavoritesIntent.RemoveFromFavorites, position)
                    }
                    R.drawable.ic_favorite_border -> {
                        viewModel.processIntents(FavoritesIntent.AddToFavorites, position)
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
            binding.sRefresh.flowRefresh().map { FavoritesIntent.SwipeOnRefresh },
            binding.rvMain.flowEndless().map { FavoritesIntent.LoadNextIntent(it) },
            binding.tvError.flowClick().map { FavoritesIntent.LoadLast }
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
