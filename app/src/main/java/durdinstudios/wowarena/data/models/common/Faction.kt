package durdinstudios.wowarena.data.models.common

import com.google.gson.annotations.SerializedName
import durdinstudios.wowarena.R

enum class Faction {
    @SerializedName("0")
    ALLIANCE,
    @SerializedName("1")
    HORDE;

    fun getFactionIcon() = when (this) {
        ALLIANCE -> R.drawable.alliance
        HORDE -> R.drawable.horde
    }

    fun getFactionTint() = when (this) {
        ALLIANCE -> R.color.color_alliance
        HORDE -> R.color.color_horde
    }
}