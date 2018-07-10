package durdinstudios.wowarena.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.dagger.BaseActivity
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.misc.argument
import durdinstudios.wowarena.misc.colorCompat
import durdinstudios.wowarena.misc.withFade
import kotlinx.android.synthetic.main.home_navigation_activity.*
import mini.Dispatcher
import javax.inject.Inject

class HomeActivity : BaseActivity() {

    @Inject
    lateinit var dispatcher: Dispatcher

    private val navAdapter by lazy { NavigationAdapter(characterName, characterRealm, region) }
    var currentTab: Int = MAIN_HOME_TAB_POSITION

    private val fromNotification by argument<Boolean>(INTENT_FROM_NOTIFICATION)
    private val characterName by argument<String>(CHARACTER_NAME)
    private val characterRealm by argument<String>(CHARACTER_REALM)
    private val characterRegion by argument<String>(CHARACTER_REGION)
    private val region by lazy { Region.valueOf(characterRegion) }

    companion object {
        const val MAIN_HOME_TAB_POSITION = 1 //The one in the middle
        const val INTENT_FROM_NOTIFICATION = "from_notification"
        const val CHARACTER_NAME = "name"
        const val CHARACTER_REALM = "realm"
        const val CHARACTER_REGION = "region"
        const val TAB_SELECTED = "navigation_tab_selected"
        fun newIntent(context: Context, tabSelected: Int = MAIN_HOME_TAB_POSITION,
                      fromNotification: Boolean = false,
                      name: String,
                      realm: String,
                      region: Region): Intent =
                Intent(context, HomeActivity::class.java).apply {
                    putExtra(TAB_SELECTED, tabSelected)
                    putExtra(INTENT_FROM_NOTIFICATION, fromNotification)
                    putExtra(CHARACTER_NAME, name)
                    putExtra(CHARACTER_REALM, realm)
                    putExtra(CHARACTER_REGION, region.name)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentTab = savedInstanceState?.getInt(TAB_SELECTED, MAIN_HOME_TAB_POSITION)
                ?: intent.getIntExtra(TAB_SELECTED, MAIN_HOME_TAB_POSITION)

        //if (fromNotification) prefs.clearNotificationInfo()

        setContentView(R.layout.home_navigation_activity)
        initializeInterface()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TAB_SELECTED, currentTab)
    }

    private fun initializeInterface() {
        val navigationAdapter = AHBottomNavigationAdapter(this, R.menu.navigation)
        navigationAdapter.setupWithBottomNavigation(bottom_navigation)
        with(bottom_navigation) {
            titleState = AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE
            accentColor = colorCompat(R.color.textColorPrimary)
            isBehaviorTranslationEnabled = true
            inactiveColor = colorCompat(R.color.textColorSecondary)
            isForceTint = true
            defaultBackgroundColor = colorCompat(R.color.wow_brown)
            currentItem = currentTab
            setOnTabSelectedListener { position, wasSelected -> onTabSelected(position, wasSelected) }
        }

        onTabSelected(currentTab, false)
    }

    private fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
        supportFragmentManager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager
                .beginTransaction()
                .withFade()
                .replace(R.id.fragment_container, navAdapter.getItem(position))
                .commit()

        //Update the analytics tag for this activity
        val tabTitle = navAdapter.getItemTitle(this, position)
        //dispatcher.dispatchOnUi(SetAnalyticsCurrentActivityAction(this, tabTitle))

        currentTab = position
        return true
    }

}
