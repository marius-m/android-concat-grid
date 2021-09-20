package com.example.concatadaptergrid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.concatadaptergrid.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            val currentDestination = navController.currentDestination
            when (menuItem.itemId) {
                R.id.page_1 -> {
                    if (currentDestination?.id != R.id.PagingFragment) {
                        navController.navigate(
                            MultiAdapterFragmentDirections
                                .actionMultiAdapterFragmentToPagingFragment(),
                        )
                        true
                    } else {
                        false
                    }
                }
                R.id.page_2 -> {
                    if (currentDestination?.id != R.id.MultiAdapterFragment) {
                        navController.navigate(
                            PagingFragmentDirections
                                .actionPagingFragmentToMultiAdapterFragment()
                        )
                        true
                    } else {
                        false
                    }
                }
                else -> false
            }
        }
    }

}
