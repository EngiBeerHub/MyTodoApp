package com.example.mytodoapp.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mytodoapp.Constants
import com.example.mytodoapp.R
import com.example.mytodoapp.ui.views.MainActivity

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

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