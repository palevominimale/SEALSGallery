package app.seals.sealsgallery.ui.main

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.seals.sealsgallery.R
import app.seals.sealsgallery.databinding.ActivityMainBinding
import app.seals.sealsgallery.ui.login.LogInDialogFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val tag = "${javaClass.simpleName}_FIREBASE"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navView: NavigationView
    private lateinit var logInDialog: LogInDialogFragment
    private val appBarSet = setOf(
        R.id.nav_my_tracks,
        R.id.nav_feed,
        R.id.nav_record,
        R.id.nav_settings,
        R.id.nav_logout,
        R.id.nav_login,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        navView = binding.navView
        val drawerLayout = binding.drawerLayout
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        auth = FirebaseAuth.getInstance()
        Log.e(tag, auth.currentUser?.uid ?: "no uid")

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            auth.signOut()
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(appBarSet, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            auth.signOut()
            signedOut()
            true
        }
        navView.menu.findItem(R.id.nav_login).setOnMenuItemClickListener {
            logInDialog.show(supportFragmentManager, "")
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        logInDialog = LogInDialogFragment()
        if(auth.currentUser != null) {
            signedIn()
        } else {
            signedOut()
        }
        findViewById<TextView>(R.id.UserNameTextView).text = auth.currentUser?.displayName ?: getString(
            R.string.nav_header_not_logged
        )
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun signedOut() {
        navView.menu.setGroupVisible(R.id.nav_group_logged_in, false)
        navView.menu.setGroupVisible(R.id.nav_group_logged_out, true)
        findViewById<ImageView>(R.id.userImageView).setImageResource(R.mipmap.ic_launcher_round)
        findViewById<TextView>(R.id.UserNameTextView).text = getString(R.string.nav_header_not_logged)
    }

    private fun signedIn() {
        navView.menu.setGroupVisible(R.id.nav_group_logged_in, true)
        navView.menu.setGroupVisible(R.id.nav_group_logged_out, false)
        var drawable : BitmapDrawable? = null
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = kotlin.runCatching { Picasso.get().load(auth.currentUser?.photoUrl).get() }
            drawable = BitmapDrawable(resources, bitmap.getOrNull())
        }.invokeOnCompletion {
            runOnUiThread {
                findViewById<ImageView>(R.id.userImageView).setImageDrawable(drawable)
            }
        }
    }
}