package durdinstudios.wowarena.core.firebase

import com.bq.masmov.reflux.dagger.AppScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.perf.FirebasePerformance
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    @AppScope
    fun providePerformance(): FirebasePerformance = FirebasePerformance.getInstance()

    @Provides
    @AppScope
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance().apply {
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        firestoreSettings = settings
    }
}