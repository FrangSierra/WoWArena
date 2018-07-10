package durdinstudios.wowarena.data.models.common

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import durdinstudios.wowarena.R

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

    fun getTextId() = when (this) {
        Race.UNKNOWN -> R.string.race_unknown
        Race.HUMAN -> R.string.race_human
        Race.ORC -> R.string.race_orc
        Race.DWARF -> R.string.race_dwarf
        Race.NIGHT_ELF -> R.string.race_night_elf
        Race.UNDEAD -> R.string.race_undead
        Race.TAUREN -> R.string.race_tauren
        Race.GNOME -> R.string.race_gnome
        Race.TROLL -> R.string.race_troll
        Race.GOBLIN -> R.string.race_goblin
        Race.BLOOD_ELF -> R.string.race_blood_elf
        Race.DRAENEI -> R.string.race_draenei
        Race.WORGEN -> R.string.race_worgen
        Race.PANDAREN_N -> R.string.race_pandaren
        Race.PANDAREN_A -> R.string.race_pandaren
        Race.PANDAREN_H -> R.string.race_pandaren
        Race.NIGHTBORNE -> R.string.race_nightborne
        Race.HIGHMOUNTAIN_TAUREN -> R.string.race_highmountain_tauren
        Race.VOID_ELF -> R.string.race_void_elf
        Race.LIGHTFORGED_DRAENEI -> R.string.race_lightforged_draenei
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