package durdinstudios.wowarena.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.codemybrainsout.onboarder.AhoyOnboarderActivity
import com.codemybrainsout.onboarder.AhoyOnboarderCard
import durdinstudios.wowarena.R
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.profile.CharacterListActivity
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
        goToCharacterList()
    }

    private fun goToCharacterList() {
        val intent = CharacterListActivity.newIntent(this)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}