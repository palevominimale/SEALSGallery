package app.seals.sealsgallery.ui.settings_extended

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.seals.sealsgallery.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragmentExtended : Fragment() {

    private val vm : SettingsFragmentExtendedViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_fragment_extended, container, false)
    }
}