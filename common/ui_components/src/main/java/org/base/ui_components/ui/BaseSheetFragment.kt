package org.base.ui_components.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.base.mvi.MviIntent
import org.base.mvi.MviView
import org.base.mvi.MviViewState
import org.base.ui_components.adapter.BaseSupportAdapter
import org.base.ui_components.adapter.managers.BaseLinearLayoutManager
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI

abstract class BaseSheetListFragment<T : Any, I : MviIntent, S : MviViewState>() : BaseSheetFragment<I, S>() {

    protected var rvMain: RecyclerView? = null
    protected var adapter: BaseSupportAdapter<T>? = null

    override fun initData() {
        initRclView()
    }

    protected open fun initRclView() {
        rvMain?.layoutManager = BaseLinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
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

abstract class BaseSheetFragment<I : MviIntent, S : MviViewState>() : BaseSheetEmptyFragment(), MviView<I, S>

// BASE Bottom Sheet FRAGMENT

abstract class BaseSheetEmptyFragment() : BottomSheetDialogFragment(), DIAware {

    override val di: DI by closestDI()

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
