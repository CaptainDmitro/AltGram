package org.captaindmitro.altgram

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.captaindmitro.altgram.databinding.ActivityAltgramBinding

@AndroidEntryPoint
class AltGramActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAltgramBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    private val getImageToUpload = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        Log.i("Main", "Uri image: $uri")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAltgramBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        bottomNavigationView = binding.bottomNav

        val navHostController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostController.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav).setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment) {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }
//            if (destination.id == R.id.selectFileFragment) {
//                getImageToUpload.launch("image/*")
//                navController.navigateUp()
//            }
        }
    }
}