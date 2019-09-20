package org.mainsoft.base.screen.fragment.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import org.mainsoft.base.activity.base.ActivityCallback
import org.mainsoft.base.screen.model.base.BaseViewModel

abstract class BaseFragment : Fragment() {

    protected open lateinit var activityCallback: ActivityCallback
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

    ////////////////////////////////////////////////////////////////////////////////////////

    protected fun openActivity(fragmentClass: Class<out Activity>, args: Bundle?) {
        val intent = Intent(activity, fragmentClass)
        intent.putExtras(args ?: Bundle())
        activityCallback.openNewActivity(intent)
    }

    protected fun openActivityResult(fragmentClass: Class<out Activity>, args: Bundle?, code: Int) {
        val intent = Intent(activity, fragmentClass)
        intent.putExtras(args ?: Bundle())
        activityCallback.openResultActivity(intent, code)
    }

    protected fun openScreen(fragmentClass: Class<out BaseFragment>, args: Bundle) {
        activityCallback.openFragment(fragmentClass, true, args)
    }

    protected fun openRootScreen(fragmentClass: Class<out BaseFragment>, args: Bundle) {
        activityCallback.openRootFragment(fragmentClass, args)
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    override fun onAttach(activity: Context) {
        super.onAttach(activity)
        try {
            activityCallback = activity as ActivityCallback
        } catch (e: ClassCastException) {
            Log.e(javaClass.simpleName, e.message, e)
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    fun showSoftKeyboard(view: View) {
        if (activity != null) {
            Handler().post {
                view.requestFocus()
                (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                        ?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    fun hideSoftKeyboard() {
        activityCallback.hideSoftKeyboard()
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    open fun onResumeFromBackStack() {
        //
    }

    open fun onRestoreFragment() {
        //
    }

    open fun tryLoadAgain() {
        //
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    abstract fun getLayout(): Int
    abstract fun initData()

    fun <V : Any> getViewModel(): V = viewModel as V
}