package durdinstudios.wowarena.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.flux.NavigationFragment
import durdinstudios.wowarena.domain.user.SetShow2vs2StatsSettingAction
import durdinstudios.wowarena.domain.user.SetShow3vs3StatsSettingAction
import durdinstudios.wowarena.domain.user.SetShowRbgStatsSettingAction
import durdinstudios.wowarena.domain.user.UserStore
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import mini.Dispatcher
import mini.select
import javax.inject.Inject


class SettingsFragment : NavigationFragment() {

    @Inject
    lateinit var dispatcher: Dispatcher
    @Inject
    lateinit var userStore: UserStore

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
            ?: inflater.inflate(R.layout.settings_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeInterface()
        listenStoreChanges()
    }

    private fun initializeInterface() {
        val onCheck = CompoundButton.OnCheckedChangeListener { button, checked ->
            when (button.id) {
                R.id.settings_2vs2_stats -> {
                    dispatcher.dispatchOnUi(SetShow2vs2StatsSettingAction(checked))
                }
                R.id.settings_3vs3_stats -> {
                    dispatcher.dispatchOnUi(SetShow3vs3StatsSettingAction(checked))
                }
                R.id.settings_rbg_stats -> {
                    dispatcher.dispatchOnUi(SetShowRbgStatsSettingAction(checked))
                }
            }
        }
        settings_2vs2_stats.setOnCheckedChangeListener(onCheck)
        settings_3vs3_stats.setOnCheckedChangeListener(onCheck)
        settings_rbg_stats.setOnCheckedChangeListener(onCheck)
        settings_contact_stats_text.setOnClickListener { onContactClick() }
        settings_contact_stats_description.setOnClickListener { onContactClick() }
        settings_about_stats_description.setOnClickListener { showAboutLibs() }
        settings_about_stats_text.setOnClickListener { showAboutLibs() }
    }

    private fun onContactClick() {
        //TODO switch for real account
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, "durdinstudios@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_contact_subject))
        }
        startActivity(Intent.createChooser(intent, getString(R.string.settings_contact_action_title)))
    }

    private fun showAboutLibs() {
        LibsBuilder()
                .withActivityStyle(Libs.ActivityStyle.DARK)
                .withActivityTitle(resources.getString(R.string.settings_about_text))
                .withAboutIconShown(true)
                .withAboutVersionShownName(true)
                .withLicenseShown(true)
                .withLibraries("Dagger2", "GooglePlayServices",
                        "OkHttp", "Retrofit", "appcompat_v7", "design", "rxjava", "firebase",
                        "glide", "moshi", "gson", "okio", "support_annotations",
                        "ahbottomnavigation, hellocharts")
                .withExcludedLibraries("hellocharts")
                .start(context!!)
    }

    private fun listenStoreChanges() {
        updateSettings(userStore.state.settings)
        userStore.flowable()
                .select { it.settings }
                .subscribe { updateSettings(it) }
                .track()
    }

    private fun updateSettings(settings: Settings) {
        settings_2vs2_stats?.isChecked = settings.show2vs2Stats
        settings_3vs3_stats?.isChecked = settings.show3vs3Stats
        settings_rbg_stats?.isChecked = settings.showRbgStats
    }
}