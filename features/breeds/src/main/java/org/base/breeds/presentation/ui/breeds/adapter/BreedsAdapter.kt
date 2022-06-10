package org.base.breeds.presentation.ui.breeds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import org.base.breeds.R
import org.base.breeds.databinding.RowBreedBinding
import org.base.common.models.presentation.BreedUi
import org.base.ui_components.adapter.BaseItemListener
import org.base.ui_components.adapter.BaseSupportAdapter
import org.base.ui_components.adapter.BaseViewHolder
import org.base.ui_components.ui.listener.setDebouncedClickListener

class BreedsAdapter(listener: BaseItemListener) : BaseSupportAdapter<BreedUi>(listener) {

    override fun getItemId(position: Int): Long = getItem(position)?.id?.hashCode()?.toLong() ?: 0L

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BreedViewHolder(
            RowBreedBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setDebouncedClickListener {
            listener?.onItem(position, it.id)
        }
        (holder as BreedViewHolder).bind(getItem(position) ?: return)
    }

    private class BreedViewHolder(
        private val binding: RowBreedBinding
    ) : BaseViewHolder<BreedUi>(binding.root) {
        override fun bind(model: BreedUi) {
            binding.apply {
                itemView.tag = model.id

                txtTitle.text = model.name
                txtDescription.text = model.description
                imgMain.load(model.image) {
                    placeholder(R.drawable.ic_breed_cat)
                    crossfade(durationMillis = 200)
                    error(R.drawable.ic_breed_cat)
                    transformations(CircleCropTransformation())
                }
                imgFlag.load(model.countryFlag) {
                    crossfade(durationMillis = 200)
                    transformations(RoundedCornersTransformation(5f))
                }
            }
        }
    }
}
