package org.mainsoft.basewithkodein.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_image.view.*
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.adapter.base.BaseSupportAdapter

class GridAdapter(data: MutableList<String>) : BaseSupportAdapter<String>(data) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        //  val v = LayoutInflater.from(parent.context).inflate(R.layout.item_image_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(vwh: RecyclerView.ViewHolder, position: Int) {
        val item: String = getItem(position)
        val vh: ViewHolder? = (vwh as? ViewHolder)
        vh?.imgBg?.layoutParams = GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        // vh?.cvMain?.layoutParams = GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        Glide.with(vh?.imgBg?.context ?: return).load(item).into(vh.imgBg)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBg: ImageView? = itemView.imgBg
        //   val cvMain: CardView? = itemView.cvMain
    }

}