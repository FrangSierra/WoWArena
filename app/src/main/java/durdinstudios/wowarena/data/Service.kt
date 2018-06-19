package durdinstudios.wowarena.data

import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.data.models.warcraft.pvp.Ranking

/**
 * Interface that represents a Service for getting data.
 */
interface Service {
    fun getPvpLeaderboard(bracket: ArenaBracket): List<Ranking>

    fun getPlayerPvpInformation(realm: String, characterName: String, locale: String): PlayerInfo

    fun savePlayerInfo(playerInfo: PlayerInfo)

    fun getPlayerInfoFromDisk(): PlayerInfo?
}