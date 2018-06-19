package durdinstudios.wowarena.data

import durdinstudios.wowarena.data.models.ArenaBracket
import durdinstudios.wowarena.data.network.PlayerInfo
import durdinstudios.wowarena.data.network.PlayerStats

/**
 * Interface that represents a Service for getting data.
 */
interface Service {
    fun getPvpLeaderboard(bracket: ArenaBracket): List<PlayerStats>

    fun getPlayerPvpInformation(realm: String, characterName: String, locale: String) : PlayerInfo

    fun savePlayerInfo(playerInfo: PlayerInfo)

    fun getPlayerInfoFromDisk(): PlayerInfo?
}