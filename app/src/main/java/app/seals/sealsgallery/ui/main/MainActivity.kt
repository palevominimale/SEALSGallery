package app.seals.sealsgallery.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.seals.sealsgallery.R
import app.seals.sealsgallery.databinding.ActivityMainBinding
import app.seals.sealsgallery.domain.bootstrap.CheckPermissions
import app.seals.sealsgallery.location.LocationService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: NavigationView
    private lateinit var navController: NavController
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    private var recordIsActive = false
    private val checkPermissions = CheckPermissions(this, this)
    private var auth = FirebaseAuth.getInstance()
    private val appBarSet = setOf(
        R.id.nav_my_tracks,
        R.id.nav_feed,
        R.id.nav_record,
        R.id.nav_settings,
        R.id.nav_logout,
        R.id.nav_login,
    )

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { signInTask ->
                        if (signInTask.isSuccessful) {
                            signedIn()
                        }
                    }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions.invoke()
        binding = ActivityMainBinding.inflate(layoutInflater)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val drawerLayout = binding.drawerLayout
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(appBarSet, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        setupNavView(navController)
        setupFab()
        if (auth.currentUser != null) {
            signedIn()
        }
    }

    private fun setupFab() {
        if(checkPermissions.invoke()) {
            binding.appBarMain.fab.setOnClickListener {
                if(navController.currentDestination?.id != R.id.nav_record) {
                    navController.navigate(R.id.nav_to_record)
                }
                if(recordIsActive) {
                    val intent = Intent(this, LocationService::class.java)
                    intent.action = getString(R.string.stop_intent)
                    startService(intent)
                    recordIsActive = false
                    binding.appBarMain.fab.setImageResource(R.drawable.radio_button_checked_40px)
                } else {
                    val intent = Intent(this, LocationService::class.java)
                    intent.action = getString(R.string.start_intent)
                    startForegroundService(intent)
                    recordIsActive = true
                    binding.appBarMain.fab.setImageResource(R.drawable.stop_circle_48px)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        findViewById<TextView>(R.id.UserNameTextView).text = auth.currentUser?.displayName ?: getString(
            R.string.nav_header_not_logged
        )
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun signedOut() {
        requireViewById<ImageView>(R.id.userImageView).setImageResource(R.drawable.no_accounts_48px)
        requireViewById<TextView>(R.id.UserNameTextView).text = getString(R.string.nav_header_not_logged)
        navView.menu.setGroupVisible(R.id.nav_group_logged_in, false)
        navView.menu.setGroupVisible(R.id.nav_group_logged_out, true)
        navController.setGraph(R.navigation.mobile_navigation_logged_out)
        binding.appBarMain.fab.isVisible = false
    }

    private fun signedIn() {
        navView.menu.setGroupVisible(R.id.nav_group_logged_in, true)
        navView.menu.setGroupVisible(R.id.nav_group_logged_out, false)
        navController.setGraph(R.navigation.mobile_navigation)
        var drawable : RoundedBitmapDrawable? = null
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = kotlin.runCatching { Picasso.get().load(auth.currentUser?.photoUrl).get() }
            drawable = RoundedBitmapDrawableFactory.create(resources, bitmap.getOrNull()).apply {
                cornerRadius = 50f
            }
        }.invokeOnCompletion {
            runOnUiThread {
                try {
                    findViewById<ImageView>(R.id.userImageView).setImageDrawable(drawable)
                    findViewById<TextView>(R.id.UserNameTextView).text = auth.currentUser?.displayName
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
        }
        binding.appBarMain.fab.isVisible = true
    }

    private fun setupNavView(navController: NavController) {
        navView = binding.navView
        navView.setupWithNavController(navController)
        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            auth.signOut()
            signedOut()
            true
        }

        navView.menu.findItem(R.id.nav_login).setOnMenuItemClickListener {
            resultLauncher.launch(googleSignInClient.signInIntent)
            true
        }
    }
}