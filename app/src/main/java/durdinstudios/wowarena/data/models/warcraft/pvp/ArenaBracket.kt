package durdinstudios.wowarena.data.models.warcraft.pvp

import com.google.gson.annotations.SerializedName

enum class ArenaBracket {
    @SerializedName("2v2")
    BRACKET_2_VS_2,
    @SerializedName("3v3")
    BRACKET_3_VS_3,
    @SerializedName("rbg")
    RBG;

    val value: String
        get() =
            when (this) {
                BRACKET_2_VS_2 -> "2v2"
                BRACKET_3_VS_3 -> "3v3"
                RBG -> "rbg"
            }
}
