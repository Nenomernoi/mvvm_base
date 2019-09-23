package org.mainsoft.base.screen.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import org.mainsoft.base.screen.model.base.BaseViewModel

abstract class BaseFragment : Fragment() {

    protected open lateinit var viewModel: BaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(getLayout(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initListeners()
        onRestore(savedInstanceState, arguments)
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    protected open fun onRestore(savedInstanceState: Bundle?, arguments: Bundle?) {
        //
    }

    protected open fun initListeners() {
        txtError?.setOnClickListener {
            tryLoadAgain()
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    protected fun showHideProgress(isShowHide: Boolean) {
        activity?.runOnUiThread {
            pbLoad?.isVisible = isShowHide
        }
    }

    open fun showHideNoData(isEmpty: Boolean) {
        txtError?.isVisible = isEmpty
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    fun showMessageError(message: String?) {
        if (message.isNullOrEmpty()) {
            return
        }
        activity?.runOnUiThread {
            Toast.makeText(activity ?: return@runOnUiThread, message, Toast.LENGTH_SHORT).show()
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    open fun tryLoadAgain() {
        //
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    abstract fun getLayout(): Int
    abstract fun initData()

    fun <V : Any> getViewModel(): V = viewModel as V
}