package org.mainsoft.base.net.response

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.mainsoft.base.db.converter.WeightConverter
import org.mainsoft.base.util.createParcel

@Entity(tableName = "breeds")
data class Breed(@PrimaryKey
                 val id: String,

                 val name: String?,
                 val alt_names: String? = null,
                 val description: String?,

                 val temperament: String?,
                 val life_span: String?,

                 val origin: String?,

                 @TypeConverters(WeightConverter::class)
                 val weight: Weight? = null,

                 val wikipedia_url: String? = null,
                 val cfa_url: String? = null,
                 val vetstreet_url: String? = null,
                 val vcahospitals_url: String? = null,

                 val adaptability: Int = 0,
                 val affection_level: Int = 0,
                 val child_friendly: Int = 0,
                 val dog_friendly: Int = 0,

                 val energy_level: Int = 0,
                 val grooming: Int = 0,
                 val health_issues: Int = 0,
                 val intelligence: Int = 0,

                 val shedding_level: Int = 0,
                 val social_needs: Int = 0,
                 val stranger_friendly: Int = 0,
                 val vocalisation: Int = 0,


                 var image_id: String? = null,
                 var image_url: String? = null,

                 var favorite: Boolean = false) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Weight::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun toString(): String {
        return "Breed(id='$id', name='$name', description='$description')"
    }

    fun getLinks(): String {
        val builder = StringBuilder()

        if (!wikipedia_url.isNullOrEmpty()) {
            builder.append("$wikipedia_url\n\n")
        }
        if (!cfa_url.isNullOrEmpty()) {
            builder.append("$cfa_url\n\n")
        }
        if (!vetstreet_url.isNullOrEmpty()) {
            builder.append("$vetstreet_url\n\n")
        }
        if (!vcahospitals_url.isNullOrEmpty()) {
            builder.append("$vcahospitals_url")
        }

        return builder.toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(alt_names)
        parcel.writeString(description)
        parcel.writeString(temperament)
        parcel.writeString(life_span)
        parcel.writeString(origin)
        parcel.writeParcelable(weight, flags)
        parcel.writeString(wikipedia_url)
        parcel.writeString(cfa_url)
        parcel.writeString(vetstreet_url)
        parcel.writeString(vcahospitals_url)
        parcel.writeInt(adaptability)
        parcel.writeInt(affection_level)
        parcel.writeInt(child_friendly)
        parcel.writeInt(dog_friendly)
        parcel.writeInt(energy_level)
        parcel.writeInt(grooming)
        parcel.writeInt(health_issues)
        parcel.writeInt(intelligence)
        parcel.writeInt(shedding_level)
        parcel.writeInt(social_needs)
        parcel.writeInt(stranger_friendly)
        parcel.writeInt(vocalisation)
        parcel.writeString(image_url)
        parcel.writeString(image_id)
        parcel.writeByte(if (favorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = createParcel { Breed(it) }
    }
}
