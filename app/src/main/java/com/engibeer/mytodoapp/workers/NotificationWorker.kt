package com.engibeer.mytodoapp.workers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.engibeer.mytodoapp.Constants
import com.example.mytodoapp.R
import com.engibeer.mytodoapp.ui.views.MainActivity

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {

        val args = Bundle()
        args.putInt(
            Constants.KEY_WORK_DATA_TASK_ID,
            inputData.getInt(Constants.KEY_WORK_DATA_TASK_ID, 0)
        )
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.taskDetailFragment)
            .setArguments(args)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.notification_channel_id)
        ).apply {
            setSmallIcon(R.drawable.ic_baseline_check_circle_outline_24)
            setContentTitle(inputData.getString(Constants.KEY_WORK_DATA_TASK_TITLE))
            setContentText(inputData.getString(Constants.KEY_WORK_DATA_TASK_CONTENT))
            priority = NotificationCompat.PRIORITY_DEFAULT
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }.build()
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(inputData.getInt(Constants.KEY_WORK_DATA_TASK_ID, 0), notification)
        }
        return Result.success()
    }
}