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
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.BracketInfo
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.data.models.warcraft.pvp.getRenderUrl
import durdinstudios.wowarena.domain.arena.ArenaStore
import durdinstudios.wowarena.domain.arena.model.ArenaStats
import durdinstudios.wowarena.domain.user.LoadUserDataAction
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.error.ErrorHandler
import durdinstudios.wowarena.misc.*
import durdinstudios.wowarena.misc.LineChartUtils.prepareChartData
import durdinstudios.wowarena.navigation.HomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.player_bracket_info.view.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.Viewport
import mini.Dispatcher
import mini.select
import javax.inject.Inject

class ProfileFragment : NavigationFragment() {

    @Inject
    lateinit var dispatcher: Dispatcher
    @Inject
    lateinit var userStore: UserStore
    @Inject
    lateinit var arenaStore: ArenaStore
    @Inject
    lateinit var errorHandler: ErrorHandler

    private val inflater: LayoutInflater by lazy {
        return@lazy activity!!.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
    private val characterName by argument<String>(CHARACTER_NAME)
    private val characterRealm by argument<String>(CHARACTER_REALM)
    private val characterRegion by argument<String>(CHARACTER_REGION)
    private val region by lazy { Region.valueOf(characterRegion) }
    private lateinit var characterInfo: CharacterInfo
    private var userData: PlayerInfo? = null
    private var arenaStats: List<ArenaStats>? = null

    companion object {
        val TAG = "profile_fragment"
        const val GRAPH_VIEWPORT_DAYS = 7
        const val CHARACTER_NAME = "name"
        const val CHARACTER_REALM = "realm"
        const val CHARACTER_REGION = "region"
        fun newInstance(characterName: String, characterRealm: String, region: Region): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(HomeActivity.CHARACTER_NAME, characterName)
                    putString(HomeActivity.CHARACTER_REALM, characterRealm)
                    putString(HomeActivity.CHARACTER_REGION, region.name)
                }
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
        characterInfo = CharacterInfo(characterName, characterRealm, region)
        initializeInterface()
        inflateChart()
        listenStoreChanges()
        arenaStore.state.arenaData[characterInfo]?.takeIf { it.isNotEmpty() }?.let { showChartIfPossible(it) }
        if (!userStore.state.playersInfo.containsKey(characterInfo)) {
            profile_scrollview.makeGone()
            loading_progress.makeVisible()
        }
        reloadUserData()
    }

    private fun showChartIfPossible(it: List<ArenaStats>) {
        val chartFilled = prepareChartData(rating_chart, it, userStore.state.settings)
        if (userStore.state.playersInfo[characterInfo] == null) return
        if (chartFilled) {
            no_data_text.makeGone()
            showChartAndAddInitialZoom()
        } else no_data_text.makeVisible()
    }

    private fun initializeInterface() {
        change_user.setOnClickListener { startActivity(CharacterListActivity.newIntent(activity!!)) }
    }

    private fun listenStoreChanges() {
        userStore.flowable()
                .select { it.playersInfo[characterInfo] }
                .subscribe {
                    userData = it
                    tryToShowProfile()
                }.track()

        arenaStore.flowable()
                .select { it.arenaData[characterInfo] }
                .filter { it.isNotEmpty() }
                .subscribe {
                    arenaStats = it
                    tryToShowProfile()
                }.track()
    }

    private fun tryToShowProfile() {
        if (userData == null || arenaStats == null) return
        setUserData(userData!!)
        showChartIfPossible(arenaStats!!)
    }

    private fun setUserData(playerInfo: PlayerInfo) {
        with(playerInfo) {
            username.text = name
            username.setTextColor(colorCompat(gameClass.getClassColor()))
            character_data.text = "$level ${getString(race.getTextId())} ${getString(gameClass.getClassTextId())}"
            //honor_kills.text = "${pvp.totalHonorableKills} Honorable Kills"
            avatar.setCircularImage(getRenderUrl(Region.EU))

            if (userStore.state.settings.show2vs2Stats) {
                inflateBracket(pvp.brackets.arena2v2)
            }
            if (userStore.state.settings.show3vs3Stats) {
                inflateBracket(pvp.brackets.arena3v3)
            }
            if (userStore.state.settings.showRbgStats) {
                inflateBracket(pvp.brackets.arenaRbg)
            }
        }
        loading_progress.makeGone()
        change_user.makeVisible()
        profile_scrollview.makeVisible()
    }

    private fun showChartAndAddInitialZoom() {
        val v = Viewport(rating_chart.maximumViewport)
        v.left = v.right - GRAPH_VIEWPORT_DAYS
        rating_chart.setCurrentViewportWithAnimation(v)
        rating_chart.makeVisible()
    }

    private fun inflateBracket(bracketInfo: BracketInfo?) {
        if (bracketInfo == null) return
        if (bracketInfo.rating == 0) return
        val bracketView = inflater.inflate(R.layout.player_bracket_info, null)
        val bracketText = TextView(context)

        bracketText.text = bracketInfo.slug.toUpperCase()
        bracketText.setTypeface(null, Typeface.BOLD)
        bracketText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        bracketText.setTextColor(colorCompat(android.R.color.white))

        with(bracketView) {
            this.ranking.text = bracketInfo.rating.toString()
            this.won_games.text = bracketInfo.seasonWon.toString()
            this.lose_games.text = bracketInfo.seasonLost.toString()
        }

        val textParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        bracket_container.addView(bracketText, bracket_container.childCount, textParams)
        bracket_container.addView(bracketView, bracket_container.childCount, layoutParams)
    }

    private fun reloadUserData() {
        dispatcher.dispatchOnUi(LoadUserDataAction(characterInfo))
        userStore.flowable()
                .observeOn(AndroidSchedulers.mainThread())
                .select { it.loadUserTask }
                .filterOne { it.isTerminal() }
                .subscribe { if (it.isFailure()) manageError(it.error!!) }
                .track()
    }

    private fun manageError(error: Throwable) {
        loading_progress.makeGone()
        errorHandler.handle(error)
        error_text.text = errorHandler.getMessageForError(error)
        error_text.makeVisible()
        loading_retry_button.makeVisible()
    }

    private fun inflateChart() {
        rating_chart.isInteractive = true
        rating_chart.zoomType = ZoomType.HORIZONTAL
        rating_chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL)
        rating_chart.isHorizontalScrollBarEnabled = true
        rating_chart.lineChartData.lines = emptyList()
    }
}