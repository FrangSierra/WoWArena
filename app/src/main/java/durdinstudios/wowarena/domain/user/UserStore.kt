package durdinstudios.wowarena.domain.user

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
 * Store that has the user info.
 */
@AppScope
class UserStore @Inject constructor(private val userController: UserController) : Store<UserState>() {

    override fun initialState(): UserState {
        return UserState(player = userController.restoreSession())
    }

    @Reducer
    fun loadUser(action: LoadUserDataAction, userState: UserState): UserState {
        if (state.loadUserTask.isRunning()) return state
        userController.getUserData(action.nick, action.realm, state.currentRegion)
        return state.copy(loadUserTask = taskRunning())
    }

    @Reducer
    fun loadUserComplete(action: LoadUserDataCompleteAction, userState: UserState): UserState {
        if (!state.loadUserTask.isRunning()) return state
        return state.copy(player = action.info, loadUserTask = action.userDataTask)
    }
}

@Module
interface UserModule {
    @Binds
    @AppScope
    @IntoMap
    @ClassKey(UserStore::class)
    fun storeToMap(store: UserStore): Store<*>

    @Binds
    @AppScope
    fun userController(userControllerImpl: UserControllerImpl): UserController
}