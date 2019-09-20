package org.mainsoft.base.db.converter

import androidx.room.TypeConverter
import org.mainsoft.base.net.response.Weight

class WeightConverter {

    @TypeConverter
    fun fromWeight(it: Weight?): String {
        return "${it?.imperial};${it?.metric}"
    }

    @TypeConverter
    fun toWeight(data: String?): Weight? {
        val list = data?.split(";")
        if (list.isNullOrEmpty()) {
            return null
        }
        return Weight(list[0], list[1])
    }
}