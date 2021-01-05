package org.mainsoft.base.adapter.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

abstract class BaseSupportAdapter<T : Any> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list by Delegates.observable(mutableListOf<T>()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

}

abstract class BaseViewHolder<T : Any>(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(model: T) {
        //
    }
}

