package com.example.uasnmp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        bottomNav = findViewById(R.id.bottomNav)
        toolbar = findViewById(R.id.toolbar)
        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        val userEmail = sharedPref.getString("email", "user@email.com")
        val userName = sharedPref.getString("nama", "Sahabat Healing")
        val headerView = navigationView.getHeaderView(0)
        val tvHeaderUser = headerView.findViewById<TextView>(R.id.tvHeaderUser)
        val tvHeaderTitle = headerView.findViewById<TextView>(R.id.tvHeaderTitle)

        tvHeaderUser.text = userEmail
        tvHeaderTitle.text = "Halo, $userName!"
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_change_password -> {
                    startActivity(Intent(this, ChangePasswordActivity::class.java))
                    true
                }

                R.id.menu_sign_out -> {
                    sharedPref.edit().clear().apply()
                    val logoutIntent = Intent(this, LoginActivity::class.java)
                    logoutIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(logoutIntent)
                    drawerLayout.closeDrawers()
                    true
                }

                else -> false
            }
        }
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_explore -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ExploreFragment())
                        .commit()
                    true
                }

                R.id.nav_favorite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, FavoriteFragment())
                        .commit()
                    true
                }

                R.id.nav_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ProfileFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
        val navigateTo = intent.getStringExtra("navigateTo")
        if (savedInstanceState == null || navigateTo == "explore") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ExploreFragment())
                .commit()
            bottomNav.selectedItemId = R.id.nav_explore
        }
    }
}
