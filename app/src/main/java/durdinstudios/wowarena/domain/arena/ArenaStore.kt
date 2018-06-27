package durdinstudios.wowarena.domain.arena

import com.bq.masmov.reflux.dagger.AppScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import durdinstudios.wowarena.domain.user.LoadUserDataCompleteAction
import durdinstudios.wowarena.domain.user.UserState
import durdinstudios.wowarena.domain.user.UserStore
import mini.Reducer
import mini.Store
import javax.inject.Inject


/**
 * Store that has the user info.
 */
@AppScope
class ArenaStore @Inject constructor(private val arenaController: ArenaController, private val userStore: UserStore) : Store<ArenaState>() {

    override fun initialState(): ArenaState {
        return ArenaState(arenaData = arenaController.getArenaStats())
    }

    @Reducer
    fun loadUserComplete(action: LoadUserDataCompleteAction, arenaState: ArenaState): ArenaState {
        if (action.task.isFailure()) return state
        return state.copy(arenaData = arenaController.getArenaStats())
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