package org.base.ui_components.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import org.base.functional_programming.Failure
import org.base.mvi.MviIntent
import org.base.mvi.MviView
import org.base.mvi.MviViewState
import org.base.network.models.exception.NetworkMiddlewareFailure
import org.base.ui_components.adapter.BaseSupportAdapter
import org.base.ui_components.adapter.decorator.BottomSpaceItemDecoration
import org.base.ui_components.adapter.managers.BaseLinearLayoutManager
import org.base.utils.OneTimeEvent
import org.base.utils.consumeOnce

// BASE LIST FRAGMENT

abstract class BaseListFragment<T : Any, I : MviIntent, S : MviViewState>(layout: Int) : BaseFragment<I, S>(layout) {

    protected var rvMain: RecyclerView? = null
    protected var adapter: BaseSupportAdapter<T>? = null

    override fun initData() {
        initRclView()
    }

    protected open fun initRclView() {
        rvMain?.layoutManager = BaseLinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        addBottomSpaceAdapter()
    }

    protected open fun addBottomSpaceAdapter() {
        rvMain?.addItemDecoration(BottomSpaceItemDecoration(40))
    }

    // ////////////////////////////////////////////////////////////////////////////////////

    protected open fun setData(data: MutableList<T>) {
        if (rvMain?.adapter == null) {
            if (adapter == null) {
                baseInitAdapter()
            } else {
                setupAdapter()
            }
            afterAddAdapter()
        }
        setItems(data)
    }

    protected open fun baseInitAdapter() {
        initAdapter()
        setupAdapter()
    }

    private fun setupAdapter() {
        rvMain?.adapter = adapter
    }

    protected open fun afterAddAdapter() {
        //
    }

    protected open fun setItems(data: MutableList<T>) {
        adapter?.list = mutableListOf<T>().apply {
            addAll(data)
        }
    }

    // /////////////////////////////////////////////////////////////////////////////////

    protected open fun clearItems() {
        val size = adapter?.list?.size ?: 0
        adapter?.list?.clear()
        activity?.runOnUiThread {
            adapter?.notifyItemRemoved(size)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
        rvMain?.layoutManager = null
    }

    // ////////////////////////////////////////////////////////////////////////////////////

    protected abstract fun initAdapter()
}

// BASE FRAGMENT

abstract class BaseEmptyFragment(layout: Int) : Fragment(layout) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initBinding(inflater, container).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       if (savedInstanceState == null) {
           initData()
           initListeners()
       }
    }

    abstract fun initListeners()
    abstract fun initData()
    abstract fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): ViewBinding
}

abstract class BaseFragment<I : MviIntent, S : MviViewState>(layout: Int) : BaseEmptyFragment(layout), MviView<I, S> {

    protected var pbLoad: RelativeLayout? = null
    protected var tvError: View? = null

    // ////////////////////////////////////////////////////////////////////////////////////////

    protected fun showHideProgress(show: Boolean) {
        pbLoad?.isVisible = show
    }

    protected fun showError(error: OneTimeEvent<Failure>?, show: Boolean) {
        tvError?.isVisible = show
        error?.let {
            it.consumeOnce { failure ->
                showMessageError(
                    message = when (failure) {
                        is NetworkMiddlewareFailure -> failure.middleWareExceptionMessage
                        is Failure.UnexpectedFailure -> failure.message
                        else -> failure.toString()
                    },
                    logOrToast = false
                )
            }
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////

    private var toast: Toast? = null

    open fun showMessageError(message: Any?, logOrToast: Boolean? = false, duration: Int = Toast.LENGTH_SHORT) {

        val text = when (message) {
            null -> null
            is String -> {
                message
            }
            is Int -> {
                getString(message)
            }
            else -> null
        }

        if (text.isNullOrEmpty()) {
            return
        }

        if (logOrToast == true) {
            Log.e(javaClass.simpleName, text)
            return
        }

        activity?.runOnUiThread {
            toast?.cancel()
            toast = Toast.makeText(activity ?: return@runOnUiThread, text, duration)
            toast?.show()
        }
    }
}
