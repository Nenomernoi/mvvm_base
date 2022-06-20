package org.base.breed.presentation.ui.breed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import org.base.breed.databinding.RowImageBinding
import org.base.common.models.presentation.ImageUi
import org.base.favorites.R
import org.base.ui_components.adapter.BaseItemListener
import org.base.ui_components.adapter.BaseSupportAdapter
import org.base.ui_components.adapter.BaseViewHolder
import org.base.ui_components.ui.listener.setDebouncedClickListener

class ImageAdapter(listener: BaseItemListener) : BaseSupportAdapter<ImageUi>(listener) {

    override fun getItemId(position: Int): Long = getItem(position)?.id?.hashCode()?.toLong() ?: 0L

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(
            RowImageBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setDebouncedClickListener {
            listener?.onItem(position, it.id)
        }
        (holder as ImageViewHolder).bind(getItem(position) ?: return)
    }

    private class ImageViewHolder(
        private val binding: RowImageBinding,
        private val listener: BaseItemListener?
    ) : BaseViewHolder<ImageUi>(binding.root) {

        override fun bind(model: ImageUi) {
            binding.apply {
                itemView.tag = model.id

                val res = if (model.idFavorite != 0L) R.drawable.ic_favorite_border_color else R.drawable.ic_favorite_border
                fbImageAdd.setImageResource(res)
                fbImageAdd.setDebouncedClickListener {
                    listener?.onItem(adapterPosition, res)
                }

                imgImageMain.load(model.url) {
                    crossfade(durationMillis = 200)
                    placeholder(R.drawable.bg_favorite_item)
                    transformations(RoundedCornersTransformation(16f))
                }
            }
        }
    }
}
