package durdinstudios.wowarena.domain.arena

import com.bq.masmov.reflux.dagger.AppScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import durdinstudios.wowarena.domain.user.LoadUserArenaDataCompleteAction
import durdinstudios.wowarena.domain.user.LoadUserDataAction
import durdinstudios.wowarena.misc.taskRunning
import mini.Reducer
import mini.Store
import javax.inject.Inject


/**
 * Store that has the user info.
 */
@AppScope
class ArenaStore @Inject constructor(private val arenaController: ArenaController) : Store<ArenaState>() {

    @Reducer
    fun loadUserArenaData(action : LoadUserDataAction) : ArenaState{
        if (state.loadArenaDataTasks[action.characterInfo]?.isRunning() == true) return state
        arenaController.getArenaStats(action.characterInfo)
        return state.copy(loadArenaDataTasks = state.loadArenaDataTasks.plus(action.characterInfo to taskRunning()))
    }

    @Reducer
    fun loadUserArenaDataComplete(action: LoadUserArenaDataCompleteAction): ArenaState {
        if (state.loadArenaDataTasks[action.characterInfo]?.isRunning() == false) return state
        return state.copy(arenaData = state.arenaData.plus(action.characterInfo to action.arenaStats),
                loadArenaDataTasks = state.loadArenaDataTasks.plus(action.characterInfo to action.task))
    }

    @Reducer
    fun downloadArenaData(action: DownloadArenaStats): ArenaState {
        if (state.downloadArenaStatsTask.isRunning()) return state
        arenaController.downloadArenaStats(action.currentCharacters)
        return state.copy(downloadArenaStatsTask = taskRunning())
    }

    @Reducer
    fun downloadArenaDataComplete(action: DownloadArenaStatsComplete): ArenaState {
        if (!state.downloadArenaStatsTask.isRunning()) return state
        return state.copy(downloadArenaStatsTask = action.task)
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