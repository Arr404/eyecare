package com.total.eyecare.ui.setting

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.total.eyecare.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val back = findViewById<ImageButton>(R.id.back_setting)
        back.setOnClickListener{
            finish()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            //TODO 11 : Update theme based on value in ListPreference
            val themePreference = findPreference<ListPreference>(getString(R.string.pref_key_dark))
            themePreference?.setOnPreferenceChangeListener { preference, newValue ->
                val state = newValue as String
                if(state == getString(R.string.pref_dark_on)){
                    updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                }else if(state == getString(R.string.pref_dark_off)){
                    updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                }else{
                    updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }


        }

        private fun updateTheme(mode: Int): Boolean {
            AppCompatDelegate.setDefaultNightMode(mode)
            requireActivity().recreate()
            return true
        }
    }
}