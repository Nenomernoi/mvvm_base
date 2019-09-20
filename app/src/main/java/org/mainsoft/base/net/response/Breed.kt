package org.mainsoft.base.net.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.mainsoft.base.db.converter.WeightConverter

@Entity(tableName = "breeds")
data class Breed(@PrimaryKey
                 val id: String,

                 val name: String,
                 val alt_names: String? = null,
                 val description: String,

                 val temperament: String,
                 val life_span: String,

                 val origin: String,

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


                 var image_url: String? = null,

                 val showFull: Boolean = false) {

    override fun toString(): String {
        return "Breed(id='$id', name='$name', description='$description')"
    }
}
