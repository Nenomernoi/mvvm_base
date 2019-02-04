package org.mainsoft.basewithkodein.adapter.base

import android.widget.BaseAdapter

abstract class BaseAbsAdapter<T : Any>(private var items: MutableList<T>, var listener: OnItemClickListener) :
        BaseAdapter() {

    constructor(items: MutableList<T>) : this(items, object : OnItemClickListener {
        override fun onItemClick(position: Int) {
            //
        }
    })

    override fun getItemId(i: Int): Long = i.toLong()

    override fun getItem(position: Int): T = items[position]

    override fun getCount(): Int = if (items.isEmpty()) 0 else items.size

    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }

    fun setItems(items: MutableList<T>) {
        this.items = items
    }

    fun getData(): MutableList<T> = items
}
