package org.mainsoft.basewithkodein.listener

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.TextView

abstract class DrawableClickListener
constructor(view: TextView, drawableIndex: Int, private val fuzz: Int = DrawableClickListener.DEFAULT_FUZZ) :
        OnTouchListener {

    companion object {
        private const val DRAWABLE_INDEX_RIGHT = 2
        private const val DEFAULT_FUZZ = 10
        private const val DEFAULT_SIZE = 4
    }

    private var drawable: Drawable? = null

    init {
        val drawables = view.compoundDrawables
        if (drawables != null && drawables.size == DEFAULT_SIZE) {
            this.drawable = drawables[drawableIndex]
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && drawable != null) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            val bounds = drawable!!.bounds
            if (this.isClickOnDrawable(x, y, v, bounds, this.fuzz)) {
                return this.onDrawableClick()
            }
        }
        return false
    }

    abstract fun isClickOnDrawable(x: Int, y: Int, view: View, drawableBounds: Rect, fuzz: Int): Boolean

    abstract fun onDrawableClick(): Boolean

    abstract class RightDrawableClickListener : DrawableClickListener {
        constructor(view: TextView) : super(view, DrawableClickListener.DRAWABLE_INDEX_RIGHT)
        constructor(view: TextView, fuzz: Int) : super(view, DrawableClickListener.DRAWABLE_INDEX_RIGHT, fuzz)

        override fun isClickOnDrawable(x: Int, y: Int, view: View, drawableBounds: Rect, fuzz: Int): Boolean {
            if (x >= view.width - view.paddingRight - drawableBounds.width() - fuzz) {
                if (x <= view.width - view.paddingRight + fuzz) {
                    if (y >= view.paddingTop - fuzz) {
                        if (y <= view.height - view.paddingBottom + fuzz) {
                            return true
                        }
                    }
                }
            }
            return false
        }
    }
}
