package durdinstudios.wowarena.data.models.common

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

enum class Race(val value: Int) {
    UNKNOWN(0),
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
    LIGHTFORGED_DRAENEI(30);

    fun getTextId(){
        when(this){
            Race.UNKNOWN -> TODO()
            Race.HUMAN -> TODO()
            Race.ORC -> TODO()
            Race.DWARF -> TODO()
            Race.NIGHT_ELF -> TODO()
            Race.UNDEAD -> TODO()
            Race.TAUREN -> TODO()
            Race.GNOME -> TODO()
            Race.TROLL -> TODO()
            Race.GOBLIN -> TODO()
            Race.BLOOD_ELF -> TODO()
            Race.DRAENEI -> TODO()
            Race.WORGEN -> TODO()
            Race.PANDAREN_N -> TODO()
            Race.PANDAREN_A -> TODO()
            Race.PANDAREN_H -> TODO()
            Race.NIGHTBORNE -> TODO()
            Race.HIGHMOUNTAIN_TAUREN -> TODO()
            Race.VOID_ELF -> TODO()
            Race.LIGHTFORGED_DRAENEI -> TODO()
        }
    }
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