package durdinstudios.wowarena.misc

import durdinstudios.wowarena.data.models.common.Faction
import durdinstudios.wowarena.data.models.warcraft.pvp.Ranking

object ArenaUtils {

    const val SEASON_GLADIATOR_MINIMUM_WONS = 150
    const val GLADIATOR_MINIMUM_WONS = 150
    const val DUELIST_MINIMUM_WONS = 50
    const val RIVAL_MINIMUM_WONS = 50
    const val CHALLENGER_MINIMUM_WONS = 50

    private const val SEASON_GLADIATOR_MINIMUM_PERCENT = 0.1F
    private const val GLADIATOR_MINIMUM_PERCENT = 0.5F
    private const val DUELIST_MINIMUM_PERCENT = 3F
    private const val RIVAL_MINIMUM_PERCENT = 10F
    private const val CHALLENGER_MINIMUM_PERCENT = 35F

    fun getSeasonGladiatorArenaCutoff(ranking: Ranking, faction: Faction) =
            getCutoff(ranking, SEASON_GLADIATOR_MINIMUM_WONS, SEASON_GLADIATOR_MINIMUM_PERCENT, faction)


    fun getGladiatorArenaCutoff(ranking: Ranking, faction: Faction) =
            getCutoff(ranking, SEASON_GLADIATOR_MINIMUM_WONS, GLADIATOR_MINIMUM_PERCENT, faction)


    fun getDuelistArenaCutoff(ranking: Ranking, faction: Faction) =
            getValidStats(DUELIST_MINIMUM_WONS, ranking, faction).last().rating

    fun getRivalArenaCutoff(ranking: Ranking, faction: Faction) =
            getCutoffBasedOnEstimation(ranking, RIVAL_MINIMUM_PERCENT, faction)

    fun getChallengerArenaCutoff(ranking: Ranking, faction: Faction) =
            getCutoffBasedOnEstimation(ranking, CHALLENGER_MINIMUM_PERCENT, faction)

    private fun getCutoff(ranking: Ranking, minWons: Int, cutoffPercent: Float, faction: Faction): Int {
        val validStats = getValidStats(minWons, ranking, faction)
        return validStats
                .take((validStats.size * cutoffPercent).toInt())
                .map { it.rating }
                .average().toInt()
    }

    private fun getCutoffBasedOnEstimation(ranking: Ranking, cutoffPercent: Float, faction: Faction): Int {
        return 0 //TODO
    }

    private fun getValidStats(neededWons: Int, ranking: Ranking, faction: Faction) = ranking.ranking
            .filter { it.seasonWins > neededWons && it.faction == faction }
            .sortedBy { it.ranking }
}
