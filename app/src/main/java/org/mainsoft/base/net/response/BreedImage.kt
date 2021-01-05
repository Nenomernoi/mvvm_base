package org.mainsoft.base.net.response

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.mainsoft.base.util.createParcel

data class BreedImage(
        override val id: String,
        override var parentId: String,
        override val url: String,
        override val width: Int,
        override val height: Int,
        val breeds: MutableList<Breed> = mutableListOf()
) : Image(id, parentId, url, width, height) {

    fun getBreed(): Breed {
        val res = breeds[0]
        res.image_url = url
        res.image_id = id
        return res
    }
}

@SuppressLint("ParcelCreator")
@Entity(tableName = "images")
open class Image(
        @PrimaryKey
        open val id: String,
        open var parentId: String,
        open val url: String,
        open val width: Int,
        open val height: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(parentId)
        parcel.writeString(url)
        parcel.writeInt(width)
        parcel.writeInt(height)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = createParcel { Image(it) }
    }

}
