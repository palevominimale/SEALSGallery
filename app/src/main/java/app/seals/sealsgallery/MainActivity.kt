package app.seals.sealsgallery

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.seals.sealsgallery.databinding.ActivityMainBinding
import app.seals.sealsgallery.ui.login.LogInDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private val TAG = "${javaClass.simpleName}_FIREBASE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navView = binding.navView
        val drawerLayout = binding.drawerLayout
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        auth = FirebaseAuth.getInstance()
        Log.e(TAG, auth.currentUser?.uid ?: "no uid")

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            auth.signOut()
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val logInDialog = LogInDialogFragment()

        if(auth.currentUser?.uid != null) {
            findViewById<TextView>(R.id.logInTextView).text = "Log out"
            findViewById<TextView>(R.id.logInTextView).setOnClickListener {
                auth.signOut()
            }
        } else {
            findViewById<TextView>(R.id.logInTextView).text = getString(R.string.nav_header_log_in)
            findViewById<TextView>(R.id.logInTextView).setOnClickListener {
                logInDialog.show(supportFragmentManager, "")
            }
        }
        findViewById<TextView>(R.id.UserNameTextView).text = auth.currentUser?.displayName ?: getString(R.string.nav_header_not_logged)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
    }
}