package durdinstudios.wowarena.data.models.common

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import durdinstudios.wowarena.R


class ClassAdapter {
    @ToJson
    fun toJson(wowClass: WoWClass): Int {
        return wowClass.value
    }

    @FromJson
    fun fromJson(value: Int): WoWClass {
        return fromInt(value)
    }

    fun fromInt(value: Int): WoWClass {
        return WoWClass.values().first { it.value == value }
    }
}

enum class WoWClass(val value: Int) {
    UNKNOWN(0),
    WARRIOR(1),
    PALADIN(2),
    HUNTER(3),
    ROGUE(4),
    PRIEST(5),
    DEATH_KNIGHT(6),
    SHAMAN(7),
    MAGE(8),
    WARLOCK(9),
    MONK(10),
    DRUID(11),
    DEMON_HUNTER(12);

    companion object {
        fun fromInt(value: Int): WoWClass {
            return WoWClass.values().first { it.value == value }
        }
    }

    fun getClassColor() = when (this) {
        UNKNOWN -> R.color.colorPrimary
        WARRIOR -> R.color.color_warrior
        PALADIN -> R.color.color_paladin
        HUNTER -> R.color.color_hunter
        ROGUE -> R.color.color_rogue
        PRIEST -> R.color.color_priest
        DEATH_KNIGHT -> R.color.color_dk
        SHAMAN -> R.color.color_shaman
        MAGE -> R.color.color_mage
        WARLOCK -> R.color.color_warlock
        MONK -> R.color.color_monk
        DRUID -> R.color.color_druid
        DEMON_HUNTER -> R.color.color_dh
    }

    fun getClassTextId() = when (this) {
        UNKNOWN -> R.string.class_unknown
        WARRIOR -> R.string.class_warrior
        PALADIN -> R.string.class_paladin
        HUNTER -> R.string.class_hunter
        ROGUE -> R.string.class_rogue
        PRIEST -> R.string.class_priest
        DEATH_KNIGHT -> R.string.class_dk
        SHAMAN -> R.string.class_shaman
        MAGE -> R.string.class_mage
        WARLOCK -> R.string.class_warlock
        MONK -> R.string.class_monk
        DRUID -> R.string.class_druid
        DEMON_HUNTER -> R.string.class_dh
    }

}