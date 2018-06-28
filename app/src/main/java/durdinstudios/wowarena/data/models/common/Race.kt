package durdinstudios.wowarena.data.models.common

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

enum class Race(val value: Int) {
    HUMAN(1),
    ORC(2),
    DWARF(3),
    NIGHT_ELF(4),
    UNDEAD(5),
    TAUREN(6),
    GNOME(7),
    TROLL(8),
    GOBLIN(9),
    BLOOD_ELF(10),
    DRAENEI(11),
    WORGEN(12),
    PANDAREN_N(24),
    PANDAREN_A(25),
    PANDAREN_H(26),
    NIGHTBORNE(27),
    HIGHMOUNTAIN_TAUREN(28),
    VOID_ELF(29),
    LIGHTFORGED_DRAENEI(30),
}


class RaceAdapter {
    @ToJson
    fun toJson(region: Race): Int {
        return region.value
    }

    @FromJson
    fun fromJson(value: Int): Race {
        return fromInt(value)
    }

    fun fromInt(value: Int): Race {
        return Race.values().first { it.value == value }
    }
}