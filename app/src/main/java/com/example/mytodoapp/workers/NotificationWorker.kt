package com.example.mytodoapp.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        // TODO: Not implemented yet.
        return Result.success()
    }
}