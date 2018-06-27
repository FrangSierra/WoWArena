package durdinstudios.wowarena.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.dagger.BaseActivity
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.misc.setLinearLayoutManager
import durdinstudios.wowarena.navigation.HomeActivity
import kotlinx.android.synthetic.main.character_list_activity.*
import mini.Dispatcher
import mini.select
import javax.inject.Inject


class CharacterListActivity : BaseActivity() {

    @Inject
    lateinit var dispatcher: Dispatcher
    @Inject
    lateinit var userStore: UserStore

    private val adapter = CharacterAdapter(::onCharacterClick)

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, CharacterListActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_list_activity)
        initializeInterface()
        listenStoreChanges()
    }

    private fun listenStoreChanges() {
        userStore.flowable()
                .select { it.currentCharacters }
                .subscribe { adapter.updateCharacters(it) }
                .track()
    }

    private fun initializeInterface() {
        add_char.setOnClickListener { addNewCharacter() }
        players_recycler.setLinearLayoutManager(this, reverseLayout = false, stackFromEnd = false)
        players_recycler.adapter = adapter
        adapter.updateCharacters(userStore.state.currentCharacters)
    }

    private fun addNewCharacter() {
        startActivity(AddCharacterActivity.newIntent(this))
    }

    private fun onCharacterClick(info: Character) {
        startActivity(HomeActivity.newIntent(this, name = info.username, realm = info.realm, region = info.region))
    }
}