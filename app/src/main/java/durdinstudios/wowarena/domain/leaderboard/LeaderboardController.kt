package durdinstudios.wowarena.domain.leaderboard

import com.bq.masmov.reflux.dagger.AppScope
import durdinstudios.wowarena.data.Service
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import mini.Dispatcher
import javax.inject.Inject

/**
 * Controller that handles leaderboards' operations.
 */
interface LeaderboardController {
    /**
     * Tries to get the latest player stats for the given leaderboard.
     */
    fun getLatestPlayerStats(bracket: ArenaBracket)
}

@AppScope
@Suppress("UndocumentedPublicClass")
class LeaderboardControllerImpl @Inject constructor(private val service: Service,
                                                    private val dispatcher: Dispatcher) : LeaderboardController {
    override fun getLatestPlayerStats(bracket: ArenaBracket) {

    }

}