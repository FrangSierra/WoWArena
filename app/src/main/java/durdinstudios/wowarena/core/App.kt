package durdinstudios.wowarena.core

import com.google.gson.Gson
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import durdinstudios.wowarena.BuildConfig
import durdinstudios.wowarena.core.dagger.AppComponent
import durdinstudios.wowarena.core.dagger.AppModule
import durdinstudios.wowarena.core.dagger.DaggerAppComponent
import durdinstudios.wowarena.core.flux.CustomLoggerInterceptor
import durdinstudios.wowarena.data.RepositoryModule
import mini.DebugTree
import mini.Grove
import mini.MiniActionReducer
import org.jetbrains.annotations.TestOnly
import kotlin.properties.Delegates

/**
 * Created by fragarsie on 12/18/17.
 */

private var appInstance: App by Delegates.notNull()

val app: App get() = appInstance

/**
 * Global application object.
 */
class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return component
    }

    val exceptionHandlers: MutableList<Thread.UncaughtExceptionHandler> = ArrayList()

    val component: AppComponent
        get() {
            if (componentInstance == null) {
                componentInstance = DaggerAppComponent.builder()
                        .appModule(AppModule(app))
                        .repositoryModule(RepositoryModule())
                        .build()
                componentInstance!!.dispatcher().actionReducer = MiniActionReducer(stores = componentInstance!!.stores())
                componentInstance!!.dispatcher().addInterceptor(CustomLoggerInterceptor(componentInstance!!.stores().values))
            }
            return componentInstance!!
        }
    private var componentInstance: AppComponent? = null

    override fun onCreate() {
        appInstance = this
        super.onCreate()

        if (BuildConfig.DEBUG) Grove.plant(DebugTree(true))

        val stores = component.stores()
        //FluxUtil.initStores(stores.values.toList())

        val exceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        exceptionHandlers.add(exceptionHandler)
        Thread.setDefaultUncaughtExceptionHandler { thread, error ->
            exceptionHandlers.forEach { it.uncaughtException(thread, error) }
        }

        component.inject(this)
    }

    @TestOnly
    @Suppress("UndocumentedPublicFunction")
    fun setAppComponent(appComponent: AppComponent) {
        componentInstance = appComponent
    }
}
