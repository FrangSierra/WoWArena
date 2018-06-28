package durdinstudios.wowarena.data.models.warcraft.pvp

import com.squareup.moshi.Json

enum class ArenaBracket {
    @Json(name = "2v2")
    BRACKET_2_VS_2,
    @Json(name = "3v3")
    BRACKET_3_VS_3,
    @Json(name = "rbg")
    RBG;

    val value: String
        get() =
            when (this) {
                BRACKET_2_VS_2 -> "2v2"
                BRACKET_3_VS_3 -> "3v3"
                RBG -> "rbg"
            }
}
