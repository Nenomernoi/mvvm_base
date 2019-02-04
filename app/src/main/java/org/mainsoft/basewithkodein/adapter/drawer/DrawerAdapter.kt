package org.mainsoft.basewithkodein.adapter.drawer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_drawer.view.*
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.adapter.base.BaseSupportAdapter
import org.mainsoft.basewithkodein.adapter.base.OnItemClickListener

class DrawerAdapter(data: MutableList<DrawerItem>, onItemClickListener: OnItemClickListener) :
        BaseSupportAdapter<DrawerItem>(data, onItemClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_drawer, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(vwh: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val item: DrawerItem = getItem(position)
        val vh: DrawerAdapter.ViewHolder = (vwh as? DrawerAdapter.ViewHolder)!!

        vh.txtTitle.setText(item.resName)
        vh.imgTitle.setImageResource(item.imgResID)
        vh.itemView.setOnClickListener { listener.onItemClick(position) }
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val txtTitle = itemView.txtTitle!!
        val imgTitle = itemView.imgTitle!!
    }

}