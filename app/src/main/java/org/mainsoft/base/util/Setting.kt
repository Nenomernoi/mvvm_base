package org.mainsoft.base.util

import co.windly.ktxprefs.annotation.DefaultString
import co.windly.ktxprefs.annotation.Prefs

@Prefs(value = "CachePreferences")
class Setting(
        @DefaultString(value = "")
        internal val favorites: String
)

fun SettingPrefs.addFavorite(id: String) {

    val list = getFavorites().split(";").toMutableList()
    if (list.contains(id)) {
        list.remove(id)
    } else {
        list.add(id)
    }
    val result = list.joinToString(";")
    setFavorites(result)
    edit()
            .putFavorites(result)
            .commit()
}

fun SettingPrefs.isFavorite(id: String): Boolean {
    return getFavorites().split(";").toMutableList().contains(id)
}