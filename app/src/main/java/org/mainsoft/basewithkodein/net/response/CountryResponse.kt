package org.mainsoft.basewithkodein.net.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.Transient
import java.util.ArrayList

@Entity
class CountryResponse() : Parcelable {

    @Id
    @Expose
    var id: Long = 0

    @Index
    @SerializedName("name")
    @Expose
    var name: String? = null
    @Index
    @SerializedName("capital")
    @Expose
    var capital: String? = null

    @Transient
    @SerializedName("relevance")
    @Expose
    var relevance: String? = null

    @Transient
    @SerializedName("currencies")
    @Expose
    var currencies: MutableList<Currency> = ArrayList()

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
        capital = parcel.readString()
        relevance = parcel.readString()
        currencies = parcel.createTypedArrayList(Currency)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(capital)
        parcel.writeString(relevance)
        parcel.writeTypedList(currencies)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CountryResponse> {
        override fun createFromParcel(parcel: Parcel): CountryResponse {
            return CountryResponse(parcel)
        }

        override fun newArray(size: Int): Array<CountryResponse?> {
            return arrayOfNulls(size)
        }
    }
}