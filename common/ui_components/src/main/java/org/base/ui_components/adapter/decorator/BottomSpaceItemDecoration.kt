package org.base.ui_components.adapter.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BottomSpaceItemDecoration(val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }
        val itemCount = state.itemCount
        if (itemPosition == itemCount - 1) {
            outRect.bottom += space
        }
    }
}
