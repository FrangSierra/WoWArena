package durdinstudios.wowarena.data.models.common

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.ToJson
import durdinstudios.wowarena.R

enum class Faction(val value : Int) {
    ALLIANCE(0),
    HORDE(1);

    fun getFactionIcon() = when (this) {
        ALLIANCE -> R.drawable.alliance
        HORDE -> R.drawable.horde
    }

    fun getFactionTint() = when (this) {
        ALLIANCE -> R.color.color_alliance
        HORDE -> R.color.color_horde
    }
}

class FactionAdapter {
    @ToJson
    fun toJson(faction: Faction): Int {
        return faction.value
    }

    @FromJson
    fun fromJson(value: Int): Faction {
        return fromInt(value)
    }

    fun fromInt(value: Int): Faction {
        return Faction.values().first { it.value == value }
    }
}