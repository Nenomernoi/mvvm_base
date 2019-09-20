package org.mainsoft.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.mainsoft.base.R
import org.mainsoft.base.adapter.base.BaseSupportAdapter
import org.mainsoft.base.adapter.base.BaseViewHolder
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.screen.model.breeds.BreedsViewModel

class BreedListAdapter(private val viewModel: BreedsViewModel) : BaseSupportAdapter<Breed>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreadViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_breed, parent, false)
        return BreadViewHolder(view, viewModel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BreadViewHolder).bind(list[position])
    }

}

class BreadViewHolder(view: View, private val viewModel: BreedsViewModel) : BaseViewHolder<Breed>(view) {

    private var txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
    private var txtDescription: TextView = itemView.findViewById(R.id.txtDescription)
    private var imgMain: ImageView = itemView.findViewById(R.id.imgMain)
    private var imgOpen: ImageView = itemView.findViewById(R.id.imgOpen)

    init {
        view.setOnClickListener {
            viewModel.openMode(adapterPosition)
        }
        imgOpen.setOnClickListener {
            viewModel.openMode(adapterPosition)
        }
        txtDescription.setOnClickListener {
            viewModel.showFullDescription(adapterPosition)
        }
    }

    override fun bind(model: Breed) {

        txtTitle.text = model.name
        txtDescription.text = model.description
        txtDescription.maxLines = if (model.showFull) Integer.MAX_VALUE else 3

        Glide.with(itemView.context)
                .load(model.image_url)
                .centerCrop()
                .placeholder(R.drawable.ic_cat)
                .error(R.drawable.ic_cat)
                .apply(RequestOptions.circleCropTransform())
                .into(imgMain)

    }

}