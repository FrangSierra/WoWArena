package durdinstudios.wowarena.data.models.warcraft.pvp

enum class PvPTier {
    UNRANKED,
    COMBATANT,
    RIVAL,
    CHALLENGER,
    DUELIST,
    GLADIATOR;

    companion object {
        fun fromRating(rating: Int): PvPTier =
            when {
                rating >= 1400 -> COMBATANT
                rating >= 1600 -> CHALLENGER
                rating >= 1800 -> RIVAL
                rating >= 2100 -> DUELIST
                rating >= 2400 -> GLADIATOR
                else -> UNRANKED
            }
    }
}