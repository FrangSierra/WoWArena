package durdinstudios.wowarena.profile

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.flux.NavigationFragment
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.BracketInfo
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.data.models.warcraft.pvp.getRenderUrl
import durdinstudios.wowarena.domain.user.LoadUserDataAction
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.misc.*
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.player_bracket_info.view.*
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
    private val inflater: LayoutInflater by lazy {
        return@lazy activity!!.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

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
        with(playerInfo) {
            username.text = name
            username.setTextColor(colorCompat(gameClass.getClassColor()))
            character_data.text = "$level ${race.name} ${gameClass.name}"
            //honor_kills.text = "${pvp.totalHonorableKills} Honorable Kills"
            avatar.setCircularImage(getRenderUrl(Region.EU))
            inflateBracket(pvp.brackets.arena2v2)
            inflateBracket(pvp.brackets.arena3v3)
            inflateBracket(pvp.brackets.arenaRbg)
        }
    }

    private fun inflateBracket(bracketInfo: BracketInfo?) {
        if (bracketInfo == null) return
        val bracketView = inflater.inflate(R.layout.player_bracket_info, null)
        val bracketText = TextView(context)

        bracketText.text = bracketInfo.slug.toUpperCase()
        bracketText.setTypeface(null, Typeface.BOLD)
        bracketText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        bracketText.setTextColor(colorCompat(android.R.color.white))

        with(bracketView) {
            this.ranking.text = bracketInfo.rating.toString()
            this.won_games.text = bracketInfo.weeklyWon.toString()
            this.lose_games.text = bracketInfo.weeklyLost.toString()
        }

        val textParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        bracket_container.addView(bracketText, bracket_container.childCount, textParams)
        bracket_container.addView(bracketView, bracket_container.childCount, layoutParams)
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