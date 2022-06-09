package org.base.favorites.presentation.ui.favorites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import org.base.common.models.presentation.FavoriteUi
import org.base.favorites.R
import org.base.favorites.databinding.RowFavoriteBinding
import org.base.ui_components.adapter.BaseItemListener
import org.base.ui_components.adapter.BaseSupportAdapter
import org.base.ui_components.adapter.BaseViewHolder
import org.base.ui_components.ui.listener.setDebouncedClickListener

class FavoriteAdapter(listener: BaseItemListener) : BaseSupportAdapter<FavoriteUi>(listener) {

    override fun getItemId(position: Int): Long = getItem(position)?.id ?: position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FavoriteViewHolder(
            RowFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setDebouncedClickListener {
            listener?.onItem(position, it.id)
        }
        (holder as FavoriteViewHolder).bind(getItem(position) ?: return)
    }

    private class FavoriteViewHolder(
        private val binding: RowFavoriteBinding,
        private val listener: BaseItemListener? = null
    ) : BaseViewHolder<FavoriteUi>(binding.root) {

        override fun bind(model: FavoriteUi) {
            binding.apply {
                itemView.tag = model.id

                val res = if (model.isFavorite) R.drawable.ic_favorite_border_color else R.drawable.ic_favorite_border
                fbAdd.setImageResource(res)
                fbAdd.setDebouncedClickListener {
                    listener?.onItem(adapterPosition, res)
                }

                imgFavoriteMain.load(model.image) {
                    crossfade(durationMillis = 200)
                    placeholder(R.drawable.bg_favorite_item)
                    transformations(RoundedCornersTransformation(16f))
                }
            }
        }
    }
}
