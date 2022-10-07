package app.seals.sealsgallery.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import app.seals.sealsgallery.R

class SettingsFragmentInner : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}