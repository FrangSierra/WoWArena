package durdinstudios.wowarena.domain.leaderboard

import com.bq.masmov.reflux.dagger.AppScope
import durdinstudios.wowarena.data.WarcraftAPIInstances
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.misc.taskFailure
import durdinstudios.wowarena.misc.taskSuccess
import io.reactivex.schedulers.Schedulers
import mini.Dispatcher
import javax.inject.Inject

/**
 * Controller that handles leaderboards' operations.
 */
interface LeaderboardController {
    /**
     * Tries to get the latest player stats for the given leaderboard.
     */
    fun getLeaderboardInfo(bracket: ArenaBracket, region: Region)
}

@AppScope
@Suppress("UndocumentedPublicClass")
class LeaderboardControllerImpl @Inject constructor(private val warcraftApi: WarcraftAPIInstances,
                                                    private val dispatcher: Dispatcher) : LeaderboardController {
    override fun getLeaderboardInfo(bracket: ArenaBracket, region: Region) {
        warcraftApi.apis[region]!!.getPvPLeaderboard(bracket.value)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ ranking ->
                dispatcher.dispatchOnUi(LoadLeaderboardCompleteAction(bracket, ranking.ranking, taskSuccess()))
            }, { error ->
                dispatcher.dispatchOnUi(LoadLeaderboardCompleteAction(bracket, emptyList(), taskFailure(error)))
            })
    }
}
