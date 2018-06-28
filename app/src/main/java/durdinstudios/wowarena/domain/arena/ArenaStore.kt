package durdinstudios.wowarena.domain.arena

import com.bq.masmov.reflux.dagger.AppScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import durdinstudios.wowarena.domain.user.LoadUserDataCompleteAction
import durdinstudios.wowarena.misc.taskRunning
import mini.Reducer
import mini.Store
import javax.inject.Inject


/**
 * Store that has the user info.
 */
@AppScope
class ArenaStore @Inject constructor(private val arenaController: ArenaController) : Store<ArenaState>() {

    override fun initialState(): ArenaState {
        return ArenaState(arenaData = arenaController.getArenaStats())
    }

    @Reducer
    fun loadUserComplete(action: LoadUserDataCompleteAction, arenaState: ArenaState): ArenaState {
        if (action.task.isFailure()) return state
        return state.copy(arenaData = arenaController.getArenaStats())
    }

    @Reducer
    fun downloadArenaData(action: DownloadArenaStats, arenaState: ArenaState): ArenaState {
        if (state.downloadArenaStatsTask.isRunning()) return state
        arenaController.downloadArenaStats(action.currentCharacters)
        return state.copy(downloadArenaStatsTask = taskRunning())
    }

    @Reducer
    fun downloadArenaDataComplete(action: DownloadArenaStatsComplete, arenaState: ArenaState): ArenaState {
        if (!state.downloadArenaStatsTask.isRunning()) return state
        return state.copy(downloadArenaStatsTask = action.task, arenaData = arenaController.getArenaStats())
    }
}

@Module
interface ArenaModule {
    @Binds
    @AppScope
    @IntoMap
    @ClassKey(ArenaStore::class)
    fun storeToMap(store: ArenaStore): Store<*>

    @Binds
    @AppScope
    fun userController(userControllerImpl: ArenaControllerImpl): ArenaController
}