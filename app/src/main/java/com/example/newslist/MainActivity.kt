package com.example.newslist

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val items = listOf(
        MyItem("Tech Giants Unite for Climate Action", "In an unprecedented move, leading technology companies have joined forces to combat climate change."),
        MyItem("Breakthrough in Cancer Research Offers New Hope", "Scientists have announced a significant breakthrough in cancer research."),
        MyItem("Global Economy Shows Signs of Recovery", "After a tumultuous few years, the global economy is showing signs of recovery."),
        MyItem("Historic Peace Agreement Reached in Middle East", "Leaders from conflicting nations have come together to sign a historic peace agreement."),
        MyItem("Innovative Education Tech Transforms Learning", "A new wave of educational technology is transforming the learning experience.")
    )

    private lateinit var listView: ListView
    private lateinit var fragmentContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        fragmentContainer = findViewById(R.id.fragmentContainer)

        listView.adapter = MyListAdapter(items) { item ->
            val fragment = ItemDetailFragment.newInstance(item)

            if (isPortraitMode()) {
                // In portrait mode, hide the ListView and show the fragment
                listView.visibility = View.GONE
                fragmentContainer.visibility = View.VISIBLE
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                // In landscape mode, replace the fragment in the fragment container
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onBackPressed() {
        if (isPortraitMode()) {
            // In portrait mode, always show the ListView on back
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
                // Show ListView and hide fragment container
                listView.visibility = View.VISIBLE
                fragmentContainer.visibility = View.GONE
            } else {
                super.onBackPressed() // Call super to exit the app
            }
        } else {
            // In landscape mode, default behavior
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                super.onBackPressed() // Call super to exit the app
            }
        }
    }

    private fun isPortraitMode(): Boolean {
        return resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
    }
}
