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

    @Reducer
    fun checkUserCredential(action: CheckUserCredentialsAction, userState: UserState): UserState {
        if (state.checkCredentialsTask.isRunning()) return state
        userController.hasUserCredentials(state)
        return state.copy(checkCredentialsTask = taskRunning())
    }

    @Reducer
    fun checkUserCredentialComplete(action: CheckUserCredentialsCompleteAction, userState: UserState): UserState {
        if (!state.checkCredentialsTask.isRunning()) return state
        return state.copy(
            token = action.token,
            player = action.playerInfo,
            checkCredentialsTask = action.checkCredentialsTask
        )
    }

    @Reducer
    fun refreshToken(action: TokenRefreshedAction, userState: UserState): UserState {
        if (action.refreshTask.isSuccessful()) {
            userController.persist(token = action.token)
        }
        return state.copy(
            token = action.token,
            player = action.customer,
            refreshTaskState = action.refreshTask
        )
    }
}

@Module
interface UserModule {
    @Binds
    @AppScope @IntoMap
    @ClassKey(UserStore::class)
    fun storeToMap(store: UserStore): Store<*>

    @Binds
    @AppScope
    fun userController(userControllerImpl: UserControllerImpl): UserController
}