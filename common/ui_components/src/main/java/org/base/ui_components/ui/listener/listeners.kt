package org.base.ui_components.ui.listener

import android.os.SystemClock
import android.view.View
import android.widget.TextView
import kotlin.math.abs
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

abstract class DebouncedClickListener(private val duration: Long = DURATION_DEFAULT) : View.OnClickListener {

    companion object {
        private var lastTimeStamp: Long = 0L
        private const val DURATION_DEFAULT = 100L
        const val DURATION_SCREEN_OPEN = 1200L
    }

    private var map = hashMapOf<Int, Long>()

    abstract fun onDebouncedClick(v: View)

    override fun onClick(v: View?) {
        v?.let {
            val currentTimeStamp = SystemClock.elapsedRealtime()
            val lastTimeStamp = map[it.id] ?: 0L
            if (abs(lastTimeStamp.minus(currentTimeStamp)) >= duration) {
                map[it.id] = currentTimeStamp
                // lastTimeStamp = currentTimeStamp
                onDebouncedClick(it)
            }
        }
    }
}

fun View.setDebouncedClickListener(
    action: (view: View) -> Unit,
) {
    this.setOnClickListener(object : DebouncedClickListener() {
        override fun onDebouncedClick(v: View) {
            action.invoke(v)
        }
    })
}

fun View.setDebouncedClickListener(
    action: (view: View) -> Unit,
    duration: Long
) {
    this.setOnClickListener(object : DebouncedClickListener(duration) {
        override fun onDebouncedClick(v: View) {
            action.invoke(v)
        }
    })
}

fun TextView.flowClick(): Flow<Unit> = callbackFlow {
    setDebouncedClickListener {
        trySend(Unit).isSuccess
    }

    awaitClose {
        setOnClickListener(null)
    }
}
