package org.captaindmitro.altgram

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import org.captaindmitro.altgram.databinding.ActivityAltgramBinding
import javax.inject.Inject

@AndroidEntryPoint
class AltGramActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAltgramBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    @Inject lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAltgramBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        bottomNavigationView = binding.bottomNav

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav).setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.loginFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.homeFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}