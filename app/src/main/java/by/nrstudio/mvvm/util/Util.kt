package by.nrstudio.mvvm.util

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.nrstudio.mvvm.R

// ///////////////// PARCEL ////////////// //

inline fun <reified T : Parcelable> createParcel(crossinline createFromParcel: (Parcel) -> T?): Parcelable.Creator<T> =
	object : Parcelable.Creator<T> {
		override fun createFromParcel(source: Parcel): T? = createFromParcel(source)
		override fun newArray(size: Int): Array<out T?> = arrayOfNulls(size)
	}

fun <T : Parcelable> Parcel.readParcelable(creator: Parcelable.Creator<T>): T? {
	return if (readString() != null) creator.createFromParcel(this) else null
}


// //////////// NAVIGATION ////////////////////////// //

fun Fragment.navigateSplash(
	actionId: Int,
	popup: Int = R.id.navigation_splash,
	bundle: Bundle = bundleOf()
) {
	this.findNavController().navigate(
		actionId, bundle,
		NavOptions.Builder()
			.setPopUpTo(
				popup,
				true
			).build()
	)
}