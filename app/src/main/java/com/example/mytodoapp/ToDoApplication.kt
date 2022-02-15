package com.example.mytodoapp

import android.app.Application
import com.example.mytodoapp.data.AppDatabase

// Application class to supply Room database instance
class ToDoApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(applicationContext)
    }
}