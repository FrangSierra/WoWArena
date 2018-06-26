package durdinstudios.wowarena.data.models.common

import com.google.gson.annotations.SerializedName
import durdinstudios.wowarena.R


enum class WoWClass {

    @SerializedName("1")
    WARRIOR,
    @SerializedName("2")
    PALADIN,
    @SerializedName("3")
    HUNTER,
    @SerializedName("4")
    ROGUE,
    @SerializedName("5")
    PRIEST,
    @SerializedName("6")
    DEATH_KNIGHT,
    @SerializedName("7")
    SHAMAN,
    @SerializedName("8")
    MAGE,
    @SerializedName("9")
    WARLOCK,
    @SerializedName("10")
    MONK,
    @SerializedName("11")
    DRUID,
    @SerializedName("12")
    DEMON_HUNTER;

    fun getClassColor() = when (this) {
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
}