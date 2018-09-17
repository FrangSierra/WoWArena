package durdinstudios.wowarena.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.codemybrainsout.onboarder.AhoyOnboarderActivity
import com.codemybrainsout.onboarder.AhoyOnboarderCard
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.analytics.FirebaseAnalytics
import durdinstudios.wowarena.R
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.profile.CharacterListActivity
import io.fabric.sdk.android.Fabric
import net.khirr.android.privacypolicy.PrivacyPolicyDialog
import javax.inject.Inject

class OnBoardingActivity : AhoyOnboarderActivity() {

    @Inject
    lateinit var userStore: UserStore

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, OnBoardingActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeOnBoarding()
    }

    private fun initializeOnBoarding() {
        val introCard = AhoyOnboarderCard(getString(R.string.onboarding_welcome_title),
            getString(R.string.onboarding_welcome_description),
            R.drawable.app_icon_white)
            .apply {
                setTitleColor(R.color.white)
                setDescriptionColor(R.color.grey_200)
                setBackgroundColor(android.R.color.transparent)
                setTitleTextSize(dpToPixels(8, this@OnBoardingActivity))
                setDescriptionTextSize(dpToPixels(6, this@OnBoardingActivity))
            }
        val charactersCard = AhoyOnboarderCard(getString(R.string.onboarding_characters_title),
            getString(R.string.onboarding_characters_description),
            R.drawable.ic_group_24dp)
            .apply {
                setTitleColor(R.color.white)
                setDescriptionColor(R.color.grey_200)
                setBackgroundColor(android.R.color.transparent)
                setTitleTextSize(dpToPixels(8, this@OnBoardingActivity))
                setDescriptionTextSize(dpToPixels(6, this@OnBoardingActivity))
            }
        val trackDataCard = AhoyOnboarderCard(getString(R.string.onboarding_track_title),
            getString(R.string.onboarding_track_description),
            R.drawable.ic_insert_chart_24dp)
            .apply {
                setTitleColor(R.color.white)
                setDescriptionColor(R.color.grey_200)
                setBackgroundColor(android.R.color.transparent)
                setTitleTextSize(dpToPixels(8, this@OnBoardingActivity))
                setDescriptionTextSize(dpToPixels(6, this@OnBoardingActivity))
            }
        val rankingCard = AhoyOnboarderCard(getString(R.string.onboarding_rating_title),
            getString(R.string.onboarding_rating_description),
            R.drawable.ic_ranking_cup)
            .apply {
                setTitleColor(R.color.white)
                setDescriptionColor(R.color.grey_200)
                setBackgroundColor(android.R.color.transparent)
                setTitleTextSize(dpToPixels(8, this@OnBoardingActivity))
                setDescriptionTextSize(dpToPixels(6, this@OnBoardingActivity))
            }

        showNavigationControls(true)
        setInactiveIndicatorColor(R.color.grey_600)
        setActiveIndicatorColor(R.color.white)
        setImageBackground(R.drawable.characters_bg)
        setFinishButtonTitle(getString(R.string.onboarding_finish_button_text))
        val pages = listOf(introCard, charactersCard, trackDataCard, rankingCard)
        setOnboardPages(pages)
    }

    override fun onFinishButtonPressed() {
        PrivacyPolicyDialog(this,
            "https://localhost/terms",
            "https://localhost/privacy").apply {
            addPoliceLine("This application sends error reports, installation and send it to a server of the Fabric.io company to analyze and process it.")
            addPoliceLine("This application sends analytics traces and send it to a server of the Firebase.google.com company to analyze and process it.")
            addPoliceLine("All details about the use of data are available in our Privacy Policies, as well as all Terms of Service links below.")
            onClickListener = object : PrivacyPolicyDialog.OnClickListener {
                override fun onAccept(isFirstTime: Boolean) {
                    goToCharacterList()
                }

                override fun onCancel() {
                    FirebaseAnalytics.getInstance(this@OnBoardingActivity).setAnalyticsCollectionEnabled(false)
                    Fabric.with(this@OnBoardingActivity, Crashlytics.Builder()
                        .core(CrashlyticsCore.Builder().disabled(true)
                            .build()).build())
                    goToCharacterList()
                }

            }
        }.show()
    }

    private fun goToCharacterList() {
        val intent = CharacterListActivity.newIntent(this)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}