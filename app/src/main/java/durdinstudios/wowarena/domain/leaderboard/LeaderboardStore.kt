package durdinstudios.wowarena.domain.leaderboard

import com.bq.masmov.reflux.dagger.AppScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.misc.taskRunning
import mini.Reducer
import mini.Store
import javax.inject.Inject

/**
 * Store that keeps track of leaderboard' requests and data.
 */
@AppScope
class LeaderboardStore @Inject constructor(private val leaderboardController: LeaderboardController,
                                           private val userStore: UserStore) : Store<LeaderboardState>() {

    @Reducer
    fun loadStats(action: LoadLeaderboardAction): LeaderboardState {
        leaderboardController.getLeaderboardInfo(action.bracket, userStore.state.currentRegion)
        return state.copy(loadRankingTask = state.loadRankingTask.plus(action.bracket to taskRunning()),
                rankingStats = state.rankingStats.plus(action.bracket to emptyList()))
    }

    @Reducer
    fun statsLoaded(action: LoadLeaderboardCompleteAction): LeaderboardState {
        return state.copy(loadRankingTask = state.loadRankingTask.plus(action.bracket to action.task),
                rankingStats = state.rankingStats.plus(action.bracket to action.stats))
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