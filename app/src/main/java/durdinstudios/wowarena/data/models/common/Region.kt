package durdinstudios.wowarena.data.models.common

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

/**
 * Represents the different regions in the Battle.net API
 */
enum class Region(val value: Int) {
    EU(0),
    US(1),
    //KR(2),
    //TW(3);
}

class RegionAdapter {
    @ToJson
    fun toJson(region: Region): Int {
        return region.value
    }

    @FromJson
    fun fromJson(value: Int): Region {
        return fromInt(value)
    }

    fun fromInt(value: Int): Region {
        return Region.values().first { it.value == value }
    }
}