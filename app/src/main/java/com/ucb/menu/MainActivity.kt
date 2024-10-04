package com.ucb.menu

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the Action Bar title and subtitle
        supportActionBar?.title = "Honkai Star Rail"
        supportActionBar?.subtitle = "Trailblaze Across the Stars"

        // Load the initial fragment - Now it's set to AnotherFragment (fragment_another_content.xml)
        if (savedInstanceState == null) {
            loadFragment(AnotherFragment())  // Set AnotherFragment as default
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_go_to_another -> {
                loadFragment(AnotherFragment())
                true
            }

            R.id.menu_show_dialog -> {
                CustomDialogFragment().show(supportFragmentManager, "CustomDialog")
                true
            }

            R.id.menu_exit -> {
                // Handle exit logic (optional submenu)
                true
            }

            R.id.menu_exit_confirm -> {
                finish() // Exit the app
                true
            }

            R.id.menu_exit_cancel -> {
                // Handle cancel exit action
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
