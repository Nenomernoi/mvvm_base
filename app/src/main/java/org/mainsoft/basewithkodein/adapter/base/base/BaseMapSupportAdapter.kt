package org.mainsoft.basewithkodein.base

abstract class BaseMapSupportAdapter<T : Any>(items: MutableList<T>, listener: OnItemClickListener) :
        BaseSupportAdapter<T>(items, listener) {

    constructor(items: MutableList<T>) : this(items, object : OnItemClickListener {
        override fun onItemClick(position: Int) {
            //
        }
    })

    private var map: HashMap<Long, Boolean> = HashMap()

    open fun getMap(): HashMap<Long, Boolean> = map

    open fun setMap(map: HashMap<Long, Boolean>) {
        this.map = map
    }

    fun getIdsSelected(): MutableList<Long> {
        val ids = mutableListOf<Long>()
        ids.addAll(map.keys)
        return ids
    }

    open fun addItem(position: Int) {
        if (isSelected(position)) {
            removeItem(position)
        } else {
            this.map.put(getItemId(position), true)
        }
    }

    private fun removeItem(position: Int) {
        this.map.remove(getItemId(position))
    }

    open fun isSelected(position: Int): Boolean {
        return this.map[getItemId(position)] != null
    }
}
