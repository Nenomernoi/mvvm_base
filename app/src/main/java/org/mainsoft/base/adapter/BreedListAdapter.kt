package org.mainsoft.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.mainsoft.base.R
import org.mainsoft.base.adapter.base.BaseSupportAdapter
import org.mainsoft.base.adapter.base.BaseViewHolder
import org.mainsoft.base.listeners.BackCallback
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.screen.model.base.BaseViewModel
import org.mainsoft.base.screen.model.breeds.BreedsViewModel
import org.mainsoft.base.util.navigate

class BreedListAdapter(private val viewModel: BreedsViewModel,
                       private val backListener: BackCallback) : BaseSupportAdapter<Breed>() {

    init {
        list = viewModel.getState().data
    }

    fun getItem(position: Int) = list[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreadViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_breed, parent, false)
        return BreadViewHolder(view, viewModel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BreadViewHolder).bind(getItem(position))
        holder.itemView.setOnClickListener { view ->
            if (view.id == R.id.fbAdd) {
                return@setOnClickListener
            }
            openBreed(view, position)
        }
    }

    private fun openBreed(view: View, position: Int) {
        view.navigate(R.id.action_breedsFragment_to_breedFragment,
                bundleOf(BaseViewModel.ARGUMENT_ID to position,
                        BaseViewModel.ARGUMENT_EXTRA to getItem(position),
                        BaseViewModel.ARGUMENT_RETURN to backListener))
    }

}

class BreadViewHolder(view: View,
                      private val viewModel: BreedsViewModel) : BaseViewHolder<Breed>(view) {

    private var txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
    private var txtDescription: TextView = itemView.findViewById(R.id.txtDescription)
    private var imgMain: ImageView = itemView.findViewById(R.id.imgMain)
    private var btnFavorite: ImageView = itemView.findViewById(R.id.fbAdd)

    init {
        btnFavorite.setOnClickListener {
            viewModel.addToFavorite(adapterPosition)
        }
    }

    override fun bind(model: Breed) {

        txtTitle.text = model.name
        txtDescription.text = model.description

        btnFavorite.setImageResource(if (model.favorite) R.drawable.ic_favorite_color else R.drawable.ic_favorite_border_color)

        Glide.with(itemView.context)
                .load(model.image_url)
                .centerCrop()
                .placeholder(R.drawable.ic_cat)
                .error(R.drawable.ic_cat)
                .apply(RequestOptions.circleCropTransform())
                .into(imgMain)

    }

}
