package app.seals.sealsgallery.ui.main

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.seals.sealsgallery.R
import app.seals.sealsgallery.databinding.ActivityMainBinding
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

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MAIN_ACTIVITY"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: NavigationView
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

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
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { signInTask ->
                        if (signInTask.isSuccessful) {
                            Log.d(TAG, "signInWithCredential:success")
                            auth.currentUser
                            signedIn()
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", signInTask.exception)
                        }
                    }
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        navView = binding.navView
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val drawerLayout = binding.drawerLayout
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {

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
            resultLauncher.launch(googleSignInClient.signInIntent)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        if(auth.currentUser != null) {
            signedIn()
        } else {
            signedOut()
        }
        findViewById<TextView>(R.id.UserNameTextView).text = auth.currentUser?.displayName ?: getString(
            R.string.nav_header_not_logged
        )
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun signedOut() {
        navView.menu.setGroupVisible(R.id.nav_group_logged_in, false)
        navView.menu.setGroupVisible(R.id.nav_group_logged_out, true)
        findViewById<ImageView>(R.id.userImageView).setImageResource(R.drawable.no_accounts_48px)
        findViewById<TextView>(R.id.UserNameTextView).text = getString(R.string.nav_header_not_logged)
    }

    private fun signedIn() {
        navView.menu.setGroupVisible(R.id.nav_group_logged_in, true)
        navView.menu.setGroupVisible(R.id.nav_group_logged_out, false)
        var drawable : RoundedBitmapDrawable? = null
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = kotlin.runCatching { Picasso.get().load(auth.currentUser?.photoUrl).get() }
            drawable = RoundedBitmapDrawableFactory.create(resources, bitmap.getOrNull()).apply {
                cornerRadius = 50f
            }
        }.invokeOnCompletion {
            runOnUiThread {
                findViewById<ImageView>(R.id.userImageView).setImageDrawable(drawable)
                findViewById<TextView>(R.id.UserNameTextView).text = auth.currentUser?.displayName
            }
        }
    }
}