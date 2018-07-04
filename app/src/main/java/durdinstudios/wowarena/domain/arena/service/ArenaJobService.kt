package durdinstudios.wowarena.domain.arena.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import durdinstudios.wowarena.core.app
import durdinstudios.wowarena.domain.arena.ArenaStore
import durdinstudios.wowarena.domain.arena.DownloadArenaStats
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.misc.filterOne
import mini.Grove
import java.util.concurrent.TimeUnit


class ArenaJobService : JobService() {
    val dispatcher = app.component.dispatcher()
    val arenaStore: ArenaStore = (app.component.stores()[ArenaStore::class.java] as ArenaStore?)!!
    val userStore: UserStore = (app.component.stores()[UserStore::class.java] as UserStore?)!!

    override fun onStartJob(params: JobParameters): Boolean {
        Grove.d { "Arena Service job called $params" }
        if (userStore.state.currentCharacters.isEmpty()) {
            jobFinished(params, true)
            return true
        }
        dispatcher.dispatchOnUi(DownloadArenaStats(userStore.state.currentCharacters))
        arenaStore.flowable()
                .filterOne { it.downloadArenaStatsTask.isTerminal() }
                .subscribe {
                    jobFinished(params, it.downloadArenaStatsTask.isFailure())
                }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        Grove.d { "Arena Service job stopped $params" }
        return true
    }

}

// schedule the start of the service every 10 - 30 seconds
fun scheduleJob(context: Context) {
    val serviceComponent = ComponentName(context, ArenaJobService::class.java)
    val builder = JobInfo.Builder(1, serviceComponent).apply {
        setPeriodic(TimeUnit.HOURS.toMillis(12)) //Periodic every 20h
        setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) // require unmetered network
        setPersisted(true)
        //setOverrideDeadline(1000)
        //setBackoffCriteria(60 * 1000, JobInfo.BACKOFF_POLICY_LINEAR).build()
        setRequiresCharging(false) // we don't care if the device is charging or not
    }
    val jobScheduler: JobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    jobScheduler.schedule(builder.build())
}