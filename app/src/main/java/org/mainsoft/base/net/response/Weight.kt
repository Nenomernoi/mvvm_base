package org.mainsoft.base.net.response

import android.os.Parcel
import android.os.Parcelable
import org.mainsoft.base.util.createParcel

class Weight(
        val imperial: String?,
        val metric: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imperial)
        parcel.writeString(metric)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = createParcel { Weight(it) }
    }
}