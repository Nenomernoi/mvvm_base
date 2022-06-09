package org.base.ui_components.adapter.managers

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

class BaseLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}

class BaseGridLayoutManager : GridLayoutManager {

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    constructor(context: Context?, spanCount: Int) : super(context, spanCount)

    constructor(context: Context?, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(
        context,
        spanCount,
        orientation,
        reverseLayout
    )

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}
