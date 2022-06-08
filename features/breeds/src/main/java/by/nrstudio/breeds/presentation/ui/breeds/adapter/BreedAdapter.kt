package by.nrstudio.breeds.presentation.ui.breeds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.nrstudio.breeds.R
import by.nrstudio.breeds.databinding.RowBreedBinding
import by.nrstudio.common.models.presentation.BreedUi
import by.nrstudio.ui_components.adapter.BaseItemListener
import by.nrstudio.ui_components.adapter.BaseSupportAdapter
import by.nrstudio.ui_components.adapter.BaseViewHolder
import by.nrstudio.ui_components.ui.listener.setDebouncedClickListener
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation

class BreedAdapter(listener: BaseItemListener) : BaseSupportAdapter<BreedUi>(listener) {

    override fun getItemId(position: Int): Long = getItem(position)?.id?.hashCode()?.toLong() ?: 0L

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BreedViewHolder(
            RowBreedBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setDebouncedClickListener {
            listener?.onItem(position, it.id)
        }
        (holder as BreedViewHolder).bind(getItem(position) ?: return)
    }

    private class BreedViewHolder(
        private val binding: RowBreedBinding,
        private val listener: BaseItemListener? = null
    ) : BaseViewHolder<BreedUi>(binding.root) {
        override fun bind(model: BreedUi) {
            binding.apply {
                itemView.tag = model.id
                fbAdd.setImageResource(if (model.isFavorite) R.drawable.ic_favorite_border_color else R.drawable.ic_favorite_border)
                fbAdd.setDebouncedClickListener {
                    listener?.onItem(adapterPosition, it.id)
                }
                txtTitle.text = model.name
                txtDescription.text = model.description
                imgMain.load(model.image) {
                    placeholder(R.drawable.ic_cat)
                    crossfade(durationMillis = 200)
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
