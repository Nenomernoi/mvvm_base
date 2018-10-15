package org.mainsoft.basewithkodein.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseSupportAdapter<T : Any>(private var items: MutableList<T>, var listener: OnItemClickListener) :
        androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    constructor(items: MutableList<T>) : this(items, object : OnItemClickListener {
        override fun onItemClick(position: Int) {
            //
        }
    })

    override fun getItemId(i: Int): Long = i.toLong()

    fun getItem(position: Int): T = items[position]

    override fun getItemCount(): Int = if (items.isEmpty()) 0 else items.size

    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }

    fun setItems(items: MutableList<T>) {
        this.items = items
    }

    fun addItems(items: MutableList<T>) {
        this.items.addAll(items)
    }

    fun setItem(position: Int, obj: T) {
        this.items[position] = obj
    }

    fun getData(): MutableList<T> = items
}
