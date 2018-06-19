package durdinstudios.wowarena.data.models.warcraft.pvp

enum class ArenaBracket {
    BRACKET_2_VS_2,
    BRACKET_3_VS_3,
    BRACKET_5_VS_5,
    RBG;

    val value: String
        get() =
            when (this) {
                BRACKET_2_VS_2 -> "2v2"
                BRACKET_3_VS_3 -> "3v3"
                BRACKET_5_VS_5 -> "5v5"
                RBG -> "rbg"
            }
}
