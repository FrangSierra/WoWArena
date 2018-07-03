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
        val introCard = AhoyOnboarderCard("Welcome",
                "Welcome to Gladius!\nThe best application to keep track of your pvp performance on World of Warcraft!",
                R.drawable.ic_ranking_cup)
                .apply {
                    setTitleColor(R.color.white)
                    setDescriptionColor(R.color.grey_200)
                    setBackgroundColor(R.color.black_transparent)
                    setTitleTextSize(dpToPixels(10, this@OnBoardingActivity))
                    setDescriptionTextSize(dpToPixels(8, this@OnBoardingActivity))
                }
        val charactersCard = AhoyOnboarderCard("Characters",
                "Add characters to your list to save and store the pvp data of multiple characters",
                R.drawable.ic_group_24dp)
                .apply {
                    setTitleColor(R.color.white)
                    setDescriptionColor(R.color.grey_200)
                    setBackgroundColor(R.color.black_transparent)
                    setTitleTextSize(dpToPixels(10, this@OnBoardingActivity))
                    setDescriptionTextSize(dpToPixels(8, this@OnBoardingActivity))
                }
        val trackDataCard = AhoyOnboarderCard("Track your rating",
                "Gladius will fetch every day the data of your selected characters to bring you stats, metrics and information regarding your pvp performance",
                R.drawable.ic_insert_chart_24dp)
                .apply {
                    setTitleColor(R.color.white)
                    setDescriptionColor(R.color.grey_200)
                    setBackgroundColor(R.color.black_transparent)
                    setTitleTextSize(dpToPixels(10, this@OnBoardingActivity))
                    setDescriptionTextSize(dpToPixels(8, this@OnBoardingActivity))
                }
        val rankingCard = AhoyOnboarderCard("Ranking",
                "Check the ranking of each bracket and region to see the best pvp players of the world",
                R.drawable.ic_ranking_cup)
                .apply {
                    setTitleColor(R.color.white)
                    setDescriptionColor(R.color.grey_200)
                    setBackgroundColor(R.color.black_transparent)
                    setTitleTextSize(dpToPixels(10, this@OnBoardingActivity))
                    setDescriptionTextSize(dpToPixels(8, this@OnBoardingActivity))
                }

        showNavigationControls(true)
        setInactiveIndicatorColor(R.color.grey_600)
        setActiveIndicatorColor(R.color.white)
        setImageBackground(R.drawable.characters_bg)
        setFinishButtonTitle("Get Started")
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