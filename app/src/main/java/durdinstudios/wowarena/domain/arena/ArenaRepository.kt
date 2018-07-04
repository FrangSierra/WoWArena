package durdinstudios.wowarena.domain.arena

import android.content.Context
import com.bq.masmov.reflux.dagger.AppScope
import com.crashlytics.android.Crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import durdinapps.rxfirebase2.RxFirestore
import durdinstudios.wowarena.core.firebase.*
import durdinstudios.wowarena.domain.arena.model.ArenaStats
import durdinstudios.wowarena.domain.user.LoadUserArenaDataCompleteAction
import durdinstudios.wowarena.misc.taskFailure
import durdinstudios.wowarena.misc.taskSuccess
import durdinstudios.wowarena.profile.Character
import durdinstudios.wowarena.profile.CharacterInfo
import durdinstudios.wowarena.profile.toCharacterInfo
import io.reactivex.schedulers.Schedulers
import mini.Dispatcher
import javax.inject.Inject


@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
interface ArenaPersistence {
    fun getArenaStats(characterInfo: CharacterInfo)
    fun saveArenaStats(stats: ArenaStats)
}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class SharedPrefsArenaPersistence @Inject constructor(val context: Context,
                                                      val dispatcher : Dispatcher,
                                                      val firestore: FirebaseFirestore) : ArenaPersistence {


    override fun getArenaStats(characterInfo: CharacterInfo) {
        RxFirestore.getCollection(firestore.character(characterInfo))
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val data = it.toObjects(FirebaseArenaStats::class.java)
                    dispatcher.dispatchOnUi(LoadUserArenaDataCompleteAction(characterInfo, data.map { it.toArenaStats() }, taskSuccess()))
                }, { error ->
                    dispatcher.dispatchOnUi(LoadUserArenaDataCompleteAction(characterInfo, emptyList(), taskFailure(error)))
                }, {
                    dispatcher.dispatchOnUi(LoadUserArenaDataCompleteAction(characterInfo, emptyList(), taskSuccess()))
                })
    }

    override fun saveArenaStats(stats: ArenaStats) {
        val pojo = stats.toFirebaseArenaStats()
        RxFirestore.setDocument(firestore.characterData(stats.character.toCharacterInfo(), stats.timestamp), pojo)
            .subscribeOn(Schedulers.io())
            .subscribe({
                //Do nothing
            }, { error ->
                Crashlytics.logException(error)
            })
    }

}


@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class ArenaRepository(private val arenaPersistence: ArenaPersistence) {

    fun getArenaStats(characterInfo: CharacterInfo) {
        arenaPersistence.getArenaStats(characterInfo)
    }

    fun saveArenaStats(stats: ArenaStats) {
        arenaPersistence.saveArenaStats(stats)
    }
}

@Module
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class ArenaRepositoryModule {
    @Provides
    @AppScope
    fun provideArenaRepository(arenaPersistence: ArenaPersistence): ArenaRepository {
        return ArenaRepository(arenaPersistence)
    }

    @Provides
    @AppScope
    fun provideArenaPersistence(prefsArenaPersistence: SharedPrefsArenaPersistence): ArenaPersistence {
        return prefsArenaPersistence
    }
}
