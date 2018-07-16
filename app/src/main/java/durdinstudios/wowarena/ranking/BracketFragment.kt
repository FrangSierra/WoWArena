package durdinstudios.wowarena.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crashlytics.android.Crashlytics
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.dagger.BaseFragment
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerBracketStats
import durdinstudios.wowarena.domain.leaderboard.LeaderboardStore
import durdinstudios.wowarena.domain.leaderboard.LoadLeaderboardAction
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.misc.*
import durdinstudios.wowarena.misc.TaskStatus.*
import durdinstudios.wowarena.profile.ProfileFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.bracket_fragment.*
import mini.Dispatcher
import mini.select
import javax.inject.Inject


class BracketFragment : BaseFragment() {

    @Inject
    lateinit var dispatcher: Dispatcher
    @Inject
    lateinit var leaderboardStore: LeaderboardStore
    @Inject
    lateinit var userStore: UserStore

    private val bracket by argument<String>(BRACKET)
    private val currentBracket by lazy { ArenaBracket.valueOf(bracket) }
    private val adapter = BracketAdapter(::onPlayerClick)

    companion object {
        val TAG = "bracket_fragment"
        const val BRACKET = "bracket"
        fun newInstance(bracket: ArenaBracket): BracketFragment {
            return BracketFragment().apply {
                val args = Bundle()
                args.putString(BRACKET, bracket.name)
                arguments = args
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = view
            ?: inflater.inflate(R.layout.bracket_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeInterface()
        listenStoreChanges()
        if (leaderboardStore.state.rankingStats[currentBracket] == null
                || leaderboardStore.state.rankingStats[currentBracket]!!.isEmpty()) {
            reloadRanking(currentBracket, userStore.state.currentRegion)
        }
    }

    private fun initializeInterface() {
        ranking_swipe!!.setOnRefreshListener { reloadRanking(currentBracket, userStore.state.currentRegion) }
        ranking_recycler.setLinearLayoutManager(context!!, reverseLayout = false, stackFromEnd = false)
        ranking_recycler.adapter = adapter
    }

    private fun listenStoreChanges() {
        leaderboardStore.flowable()
                .select { it.rankingStats[currentBracket] }
                .subscribe {
                    adapter.updateRanking(it)
                }
                .track()

        leaderboardStore.flowable()
                .select { it.loadRankingTask[currentBracket] }
                .subscribe {
                    when (it.status) {
                        RUNNING -> if (!ranking_swipe.isRefreshing) loading_progress?.makeVisible()
                        SUCCESS -> loading_progress?.makeGone()
                        ERROR -> Crashlytics.logException(it.error!!)
                    }
                }.track()
        userStore.flowable()
                .select { it.currentRegion }
                .skip(1) //Skip first
                .subscribe {
                    reloadRanking(currentBracket, it)
                }.track()
    }

    private fun reloadRanking(bracket: ArenaBracket, region: Region) {
        dispatcher.dispatchOnUi(LoadLeaderboardAction(true, region, bracket))
        leaderboardStore.flowable()
                .observeOn(AndroidSchedulers.mainThread())
                .select { it.loadRankingTask[bracket] }
                .filterOne { it.isTerminal() }
                .subscribe {
                    if (it.isFailure()) {
                        //manage
                    }
                    ranking_swipe.takeIf { it.isRefreshing }?.isRefreshing = false
                }.track()
    }

    private fun onPlayerClick(data: PlayerBracketStats) {
        activity!!.supportFragmentManager!!
                .beginTransaction()
                .withFade()
                .replace(R.id.fragment_container, ProfileFragment.newInstance(data.name, data.realmName, userStore.state.currentRegion),
                        ProfileFragment.TAG)
                .addToBackStack(null)
                .commit()
    }
}
