package durdinstudios.wowarena.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.flux.NavigationFragment
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.domain.user.LoadUserDataAction
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.misc.argument
import durdinstudios.wowarena.misc.filterOne
import durdinstudios.wowarena.misc.makeGone
import durdinstudios.wowarena.misc.makeVisible
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import mini.Dispatcher
import mini.select
import javax.inject.Inject

class ProfileFragment : NavigationFragment() {

    @Inject
    lateinit var dispatcher: Dispatcher
    @Inject
    lateinit var userStore: UserStore

    private val characterName by argument<String>(CHARACTER_NAME)
    private val realm by argument<String>(REALM_NAME)

    companion object {
        val TAG = "profile_fragment"
        const val CHARACTER_NAME = "character"
        const val REALM_NAME = "realm"
        fun newInstance(characterName: String, realmName: String): ProfileFragment {
            return ProfileFragment().apply {
                val args = Bundle()
                args.putString(CHARACTER_NAME, characterName)
                args.putString(REALM_NAME, realmName)
                arguments = args
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity!!.toolbar.title = getString(R.string.navigation_profile)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = view
            ?: inflater.inflate(R.layout.profile_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeInterface()
        listenStoreChanges()
        //if (userStore.state.player == null)
            reloadUserData()
    }

    private fun initializeInterface() {

    }

    private fun listenStoreChanges() {
        userStore.flowable()
                .select { it.player }
                .subscribe {
                    setUserData(it)
                }
                .track()
    }

    private fun setUserData(playerInfo: PlayerInfo) {

    }

    private fun reloadUserData() {
        loading_progress.makeVisible()
        dispatcher.dispatchOnUi(LoadUserDataAction(characterName, realm))
        userStore.flowable()
                .observeOn(AndroidSchedulers.mainThread())
                .select { it.loadUserTask }
                .filterOne { it.isTerminal() }
                .subscribe {
                    if (it.isFailure()) {
                        //manage
                    }
                    loading_progress.makeGone()
                }.track()
    }
}