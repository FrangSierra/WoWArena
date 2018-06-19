package durdinstudios.wowarena.domain.leaderboard

import com.bq.masmov.reflux.dagger.AppScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import durdinstudios.wowarena.misc.taskRunning
import mini.Reducer
import mini.Store
import javax.inject.Inject

/**
 * Store that keeps track of leaderboard' requests and data.
 */
@AppScope
class LeaderboardStore @Inject constructor(private val leaderboardController: LeaderboardController) : Store<LeaderboardState>() {

    @Reducer
    fun loadStats(action: LoadLeaderboardAction, state: LeaderboardState): LeaderboardState {
        leaderboardController.getLatestPlayerStats(action.bracket)
        return state.copy(loadStatsTask = taskRunning())
    }

    @Reducer
    fun statsLoaded(action: LoadLeaderboardCompleteAction, state: LeaderboardState): LeaderboardState {
        return state.copy(action.stats, action.task)
    }
}

@Module
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
interface LeaderboardModule {
    @Binds
    @AppScope
    @IntoMap
    @ClassKey(LeaderboardStore::class)
    fun storeToMap(store: LeaderboardStore): Store<*>

    @Binds
    @AppScope
    fun provideleaderboardController(leaderboardController: LeaderboardControllerImpl): LeaderboardController
}