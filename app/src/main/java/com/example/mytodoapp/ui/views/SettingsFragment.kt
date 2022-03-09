package com.example.mytodoapp.ui.views

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.preference.PreferenceFragmentCompat
import com.example.mytodoapp.R

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == getString(R.string.preference_key_theme)) {
            when (sharedPreferences?.getString(key, "default")) {
                "default" -> {
                    setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                }
                "light" -> {
                    setDefaultNightMode(MODE_NIGHT_NO)
                }
                "dark" -> {
                    setDefaultNightMode(MODE_NIGHT_YES)
                }
            }
        }
    }
}