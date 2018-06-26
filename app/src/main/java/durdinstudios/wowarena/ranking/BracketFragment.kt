package durdinstudios.wowarena.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.dagger.BaseFragment
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerBracketStats
import durdinstudios.wowarena.domain.leaderboard.LeaderboardStore
import durdinstudios.wowarena.domain.leaderboard.LoadLeaderboardAction
import durdinstudios.wowarena.misc.argument
import durdinstudios.wowarena.misc.filterOne
import durdinstudios.wowarena.misc.makeGone
import durdinstudios.wowarena.misc.setLinearLayoutManager
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
                || leaderboardStore.state.rankingStats[currentBracket]!!.isEmpty())
            reloadRanking(currentBracket)
    }

    private fun initializeInterface() {
        ranking_swipe!!.setOnRefreshListener { reloadRanking(currentBracket) }
        ranking_recycler.setLinearLayoutManager(context!!, reverseLayout = false, stackFromEnd = false)
        ranking_recycler.adapter = adapter
    }

    private fun listenStoreChanges() {
        leaderboardStore.flowable()
                .select { it.rankingStats[currentBracket] }
                .filter { it.isNotEmpty() }
                .subscribe { adapter.updateRanking(it) }
                .track()
    }

    private fun reloadRanking(bracket: ArenaBracket) {
        dispatcher.dispatchOnUi(LoadLeaderboardAction(true, bracket))
        leaderboardStore.flowable()
                .observeOn(AndroidSchedulers.mainThread())
                .select { it.loadRankingTask[bracket] }
                .filterOne { it.isTerminal() }
                .subscribe {
                    if (it.isFailure()) {
                        //manage
                    }
                    loading_progress.makeGone()
                    ranking_swipe?.takeIf { it.isRefreshing }?.isRefreshing = false
                }.track()
    }

    private fun onPlayerClick(data: PlayerBracketStats) {
        //TODO do something?
    }
}
