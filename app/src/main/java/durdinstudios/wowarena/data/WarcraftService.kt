package durdinstudios.wowarena.data

import com.bq.masmov.reflux.dagger.AppScope
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.data.models.warcraft.pvp.Ranking
import io.reactivex.Single
import javax.inject.Inject

/**
 * Interface that represents a Service for getting data.
 */
interface WarcraftService {
    fun getPvpLeaderboard(bracket: ArenaBracket, region: Region): Single<Ranking>

    fun getPlayerPvpInformation(realm: String, characterName: String, region: Region): Single<PlayerInfo>
}

/**
 * Interface to provide application data. It comes from disk or network.
 */

@AppScope
class WarcraftServiceImpl @Inject constructor(private val warcraftApi: WarcraftAPIInstances) : WarcraftService {
    override fun getPvpLeaderboard(bracket: ArenaBracket, region: Region): Single<Ranking> {
        return warcraftApi.apis[region]!!.getPvPLeaderboard(bracket.value)
    }

    override fun getPlayerPvpInformation(realm: String, characterName: String, region: Region): Single<PlayerInfo> {
        return warcraftApi.apis[region]!!.getPlayerPvpInfo(characterName, realm)
    }
}