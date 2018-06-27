package durdinstudios.wowarena.core

import android.content.Intent
import android.os.Bundle
import durdinstudios.wowarena.core.dagger.BaseActivity
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.navigation.HomeActivity
import durdinstudios.wowarena.profile.CharacterListActivity
import mini.Dispatcher
import javax.inject.Inject


/**
 * First screen showed in the app, it will be showed during the time that the user is Logging in.
 */
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var userStore: UserStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (userStore.state.player != null) {
            goToHome()
        } else {
            goToCharacterList()
        }
    }

    private fun goToHome() {
        val intent = HomeActivity.newIntent(this)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun goToCharacterList() {
        val intent = CharacterListActivity.newIntent(this)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
