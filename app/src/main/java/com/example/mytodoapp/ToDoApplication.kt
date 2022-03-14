package com.example.mytodoapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.mytodoapp.data.AppDatabase

// Application class to supply Room database instance
class ToDoApplication : Application() {

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Notification Channel
        createNotificationChannel()

        // Initialize Room database
        database = AppDatabase.getInstance(applicationContext)

        // Set up theme
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        when (sharedPreferences.getString(getString(R.string.preference_key_theme), "default")) {
            "default" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            "light" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            "dark" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.notification_channel_name)
        val descriptionText = getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel(getString(R.string.notification_channel_id), name, importance)
        channel.description = descriptionText
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}