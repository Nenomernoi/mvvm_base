package org.mainsoft.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.mainsoft.base.R
import org.mainsoft.base.adapter.base.BaseSupportAdapter
import org.mainsoft.base.adapter.base.BaseViewHolder
import org.mainsoft.base.screen.model.grid.GridViewModel

class GridAdapter(private val viewModel: GridViewModel) : BaseSupportAdapter<String>() {

    init {
        list = viewModel.getState().data
    }

    fun getItem(position: Int) = list[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_image, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GridViewHolder).bind(getItem(position))
        holder.itemView.setOnClickListener { view ->
            if (view.id == R.id.fbAdd) {
                return@setOnClickListener
            }
            //
        }
    }
}

class GridViewHolder(view: View) : BaseViewHolder<String>(view) {

    private var imgBg: ImageView = itemView.findViewById(R.id.imgBg)

    override fun bind(model: String) {

        Glide.with(itemView.context)
                .load(model)
                .centerCrop()
                .placeholder(R.drawable.ic_cat)
                .error(R.drawable.ic_cat)
                .into(imgBg)

    }

}