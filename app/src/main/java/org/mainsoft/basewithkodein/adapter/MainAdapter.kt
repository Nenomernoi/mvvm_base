package org.mainsoft.basewithkodein.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_base.view.txtCode
import kotlinx.android.synthetic.main.item_base.view.txtName
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.base.BaseSupportAdapter
import org.mainsoft.basewithkodein.base.OnItemClickListener
import org.mainsoft.basewithkodein.net.response.CountryResponse

class MainAdapter(data: MutableList<CountryResponse>, onItemClickListener: OnItemClickListener) :
        BaseSupportAdapter<CountryResponse>(data, onItemClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_base, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(vwh: RecyclerView.ViewHolder, position: Int) {
        val item: CountryResponse = getItem(position)
        val vh: ViewHolder = (vwh as? ViewHolder)!!

        vh.txtName.text = item.name
        vh.txtCode.text = item.capital
        vh.itemView.setOnClickListener({ listener.onItemClick(position) })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.txtName!!
        val txtCode = itemView.txtCode!!
    }

}