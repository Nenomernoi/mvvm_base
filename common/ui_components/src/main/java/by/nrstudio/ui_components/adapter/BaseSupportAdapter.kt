package by.nrstudio.ui_components.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

abstract class BaseAdapter<T : Any>() : BaseSupportAdapter<T>()

abstract class BaseSupportAdapter<T : Any>(protected val listener: BaseItemListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list by Delegates.observable(mutableListOf<T>()) { _, old, new ->
        onUpdateList()
        when {
            old.size > 0 && new.size == 0 -> notifyItemRemoved(new.size)
            old.size == 0 && new.size > 0 -> notifyItemInserted(new.size)
            old.size == new.size -> notifyDataSetChanged()
            old.size > new.size && new.size > 0 -> {
                notifyItemRangeRemoved(new.size - 1, old.size)
            }
            else -> notifyItemRangeChanged(0, new.size)
        }
    }

    fun getData() = list

    override fun getItemCount() = list.size

    protected open fun onUpdateList() {
        // to do
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    protected fun getItem(position: Int) = list.getOrNull(position)

    fun removeItem(position: Int) {
        if (list.isEmpty() || list.size <= position) {
            return
        }
        list.removeAt(position)
        super.notifyItemRemoved(position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is BaseViewHolder<*>) {
            holder.clearImages()
        }
    }
}

abstract class BaseViewHolder<T : Any>(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(model: T) {
        //
    }

    open fun clearImages() {
        //
    }
}

interface BaseItemListener {
    fun onItem(position: Int, action: Int)
}
