package durdinstudios.wowarena.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.flux.NavigationFragment
import kotlinx.android.synthetic.main.toolbar.*
import mini.Dispatcher
import javax.inject.Inject


class SettingsFragment : NavigationFragment() {

    @Inject
    lateinit var dispatcher: Dispatcher

    companion object {
        val TAG = "settings_fragment"
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        activity!!.toolbar.title = getString(R.string.navigation_settings)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = view
            ?: inflater.inflate(R.layout.bracket_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //initializeInterface()
        //listenStoreChanges()
        //if (leaderboardStore.state.rankingStats[currentBracket] == null
        //        || leaderboardStore.state.rankingStats[currentBracket]!!.isEmpty())
        //    reloadProfile()
    }
}