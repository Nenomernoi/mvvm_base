package org.base.breeds.presentation.ui.breeds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.base.breed.presentation.ui.breed.BredFragment
import org.base.breeds.R
import org.base.breeds.databinding.FragmentBreedsBinding
import org.base.breeds.presentation.ui.breeds.adapter.BreedsAdapter
import org.base.breeds.presentation.ui.breeds.intent.BreedsIntent
import org.base.common.models.presentation.BreedUi
import org.base.mvi.Status
import org.base.ui_components.BaseApp
import org.base.ui_components.adapter.BaseItemListener
import org.base.ui_components.adapter.listeners.flowEndless
import org.base.ui_components.adapter.listeners.flowRefresh
import org.base.ui_components.ui.BaseListFragment
import org.base.ui_components.ui.listener.flowClick
import org.kodein.di.instance

@FlowPreview
@ExperimentalCoroutinesApi
class BreedsFragment : BaseListFragment<BreedUi, BreedsIntent, BreedsUiState>(R.layout.fragment_breeds) {

    private lateinit var binding: FragmentBreedsBinding
    private val viewModel: BreedsViewModel by BaseApp.di.instance()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBreedsBinding {
        binding = FragmentBreedsBinding.inflate(inflater, container, false).apply {
            this@BreedsFragment.rvMain = rvMain
            this@BreedsFragment.pbLoad = pbLoad
            this@BreedsFragment.tvError = tvError
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
        adapter = BreedsAdapter(object : BaseItemListener {
            override fun onItem(position: Int, action: Int) {
                BredFragment()
                    .apply {
                        arguments = bundleOf(BredFragment.BREED_ID to viewModel.uiState.value.data[position].id)
                    }
                    .show(childFragmentManager, "BreedDetails")
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
                        is BreedsIntent.LoadNextIntent -> intent.page
                        else -> Unit
                    }
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun intents(): Flow<BreedsIntent> {
        val flowIntents = listOf(
            binding.sRefresh.flowRefresh().map { BreedsIntent.SwipeOnRefreshIntent },
            binding.rvMain.flowEndless().map { BreedsIntent.LoadNextIntent(it) },
            binding.tvError.flowClick().map { BreedsIntent.LoadLastIntent }
        )
        return flowIntents.asFlow().flattenMerge(flowIntents.size)
    }

    override fun render(state: BreedsUiState) {
        with(state) {
            setData(state.data)
            showHideProgress(state.status == Status.LOADING)
            showError(error, state.status == Status.ERROR)
        }
    }
}
