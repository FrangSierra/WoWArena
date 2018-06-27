package durdinstudios.wowarena.domain.arena.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent


class ArenaJobService : JobService() {

    override fun onStartJob(params: JobParameters): Boolean {
        val service = Intent(applicationContext, ArenaJobService::class.java)
        applicationContext.startService(service)
        scheduleJob(applicationContext) // reschedule the job
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }

    companion object {
        private val TAG = "SyncService"
    }

}

// schedule the start of the service every 10 - 30 seconds
private fun scheduleJob(context: Context) {
    val serviceComponent = ComponentName(context, ArenaJobService::class.java)
    val builder = JobInfo.Builder(0, serviceComponent)
    builder.setMinimumLatency((1 * 1000).toLong()) // wait at least
    builder.setOverrideDeadline((3 * 1000).toLong()) // maximum delay
    builder.set
    //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
    //builder.setRequiresDeviceIdle(true); // device should be idle
    //builder.setRequiresCharging(false); // we don't care if the device is charging or not
    val jobScheduler : JobScheduler = context.applicationContext.getSystemService(JobScheduler::class.java)
    jobScheduler.schedule(builder.build())
}