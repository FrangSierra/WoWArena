package durdinstudios.wowarena.data.models.common

import com.google.gson.annotations.SerializedName

enum class Race {
    @SerializedName("1")
    HUMAN,
    @SerializedName("2")
    ORC,
    @SerializedName("3")
    DWARF,
    @SerializedName("4")
    NIGHT_ELF,
    @SerializedName("5")
    UNDEAD,
    @SerializedName("6")
    TAUREN,
    @SerializedName("7")
    GNOME,
    @SerializedName("8")
    TROLL,
    @SerializedName("9")
    GOBLIN,
    @SerializedName("10")
    BLOOD_ELF,
    @SerializedName("11")
    DRAENEI,
    @SerializedName("12")
    WORGEN,
    @SerializedName("24")
    PANDAREN_N,
    @SerializedName("25")
    PANDAREN_A,
    @SerializedName("26")
    PANDAREN_H,
    @SerializedName("27")
    NIGHTBORNE,
    @SerializedName("28")
    HIGHMOUNTAIN_TAUREN,
    @SerializedName("29")
    VOID_ELF,
    @SerializedName("30")
    LIGHTFORGED_DRAENEI,
}