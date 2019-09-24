package org.mainsoft.base.util

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import org.mainsoft.base.R


fun View.navigate(actionId: Int, bundle: Bundle? = null) {
    Navigation.findNavController(this).navigate(actionId, bundle)
}

fun Fragment.navigate(actionId: Int, bundle: Bundle? = null) {
    findNavController().navigate(actionId, bundle)
}

fun Fragment.onBack() {
    this.activity?.let { activity ->
        activity.hideKeyboard()
        findNavController().popBackStack()
    }
}

fun Fragment.hideKeyboard() {
    this.activity?.let { activity ->
        activity.currentFocus?.let { view ->
            (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {
                it.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}

fun Fragment.showKeyboard(view: View) {
    this.activity?.let { activity ->
        Handler().post {
            view.requestFocus()
            (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                    ?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

fun Activity.hideKeyboard() {
    this.let { activity ->
        activity.currentFocus?.let { view ->
            (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {
                it.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}

inline fun <reified T : Parcelable> createParcel(crossinline createFromParcel: (Parcel) -> T?): Parcelable.Creator<T> =
        object : Parcelable.Creator<T> {
            override fun createFromParcel(source: Parcel): T? = createFromParcel(source)
            override fun newArray(size: Int): Array<out T?> = arrayOfNulls(size)
        }

fun <T : Parcelable> Parcel.readParcelable(creator: Parcelable.Creator<T>): T? {
    return if (readString() != null) creator.createFromParcel(this) else null
}