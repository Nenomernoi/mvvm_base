package org.base.breed.presentation.ui.breed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.base.breed.databinding.FragmentBreedBinding
import org.base.breed.presentation.ui.breed.adapter.ImageAdapter
import org.base.breed.presentation.ui.breed.intent.ImagesIntent
import org.base.common.models.presentation.BreedFullUi
import org.base.common.models.presentation.ImageUi
import org.base.favorites.R
import org.base.ui_components.BaseApp
import org.base.ui_components.adapter.BaseItemListener
import org.base.ui_components.adapter.listeners.flowEndless
import org.base.ui_components.adapter.managers.BaseLinearLayoutManager
import org.base.ui_components.ui.BaseSheetListFragment
import org.base.ui_components.ui.listener.setDebouncedClickListener
import org.kodein.di.instance

@FlowPreview
@ExperimentalCoroutinesApi
class BredFragment : BaseSheetListFragment<ImageUi, ImagesIntent, BreedUiState>() {

    companion object {
        const val BREED_ID = "breedId"
    }

    private lateinit var binding: FragmentBreedBinding
    private val viewModel: BreedViewModel by BaseApp.di.instance()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBreedBinding {
        binding = FragmentBreedBinding.inflate(inflater, container, false).apply {
            this@BredFragment.rvMain = rvMain
        }
        return binding
    }

    override fun initData() {
        super.initData()
        requireArguments().getString(BREED_ID)?.let { viewModel.processIntents(ImagesIntent.InitialIntent, it) }
        collectUiState()
    }

    override fun initRclView() {
        binding.rvMain.layoutManager = BaseLinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                render(state)
            }
        }
    }

    override fun initAdapter() {
        adapter = ImageAdapter(object : BaseItemListener {
            override fun onItem(position: Int, action: Int) {
                when (action) {
                    R.drawable.ic_favorite_border_color -> {
                        viewModel.processIntents(ImagesIntent.RemoveFromFavoritesIntent, position)
                    }
                    R.drawable.ic_favorite_border -> {
                        viewModel.processIntents(ImagesIntent.AddToFavoritesIntent, position)
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
        binding.imgClose.setDebouncedClickListener {
            dismiss()
        }
        intents()
            .onEach { intent ->
                viewModel.processIntents(
                    intent,
                    when (intent) {
                        is ImagesIntent.LoadNextIntent -> intent.page
                        else -> Unit
                    }
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun intents(): Flow<ImagesIntent> {
        val flowIntents = listOf(
            binding.rvMain.flowEndless().map { ImagesIntent.LoadNextIntent(it) },
        )
        return flowIntents.asFlow().flattenMerge(flowIntents.size)
    }

    private fun setFields(model: BreedFullUi?) {
        binding.apply {
            txtTitle.text = model?.name
            txtDescription.text = model?.description

            imgMain.load(model?.image) {
                placeholder(R.drawable.bg_favorite_item)
                crossfade(durationMillis = 200)
                error(R.drawable.bg_favorite_item)
                transformations(RoundedCornersTransformation(topLeft = 16f, topRight = 16f))
            }
            imgFlag.load(model?.countryFlag) {
                crossfade(durationMillis = 200)
                transformations(RoundedCornersTransformation(5f))
            }
        }
    }

    override fun render(state: BreedUiState) {
        with(state) {
            setData(state.data)
            setFields(state.model)
        }
    }
}
