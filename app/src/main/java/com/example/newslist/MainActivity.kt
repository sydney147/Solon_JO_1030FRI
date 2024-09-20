package com.example.newslist

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val items = listOf(
        MyItem("Tech Giants Unite for Climate Action", "In an unprecedented move, leading technology companies have joined forces to combat climate change. The coalition aims to reduce carbon emissions by 50% by 2030, implementing innovative solutions to enhance sustainability across the industry."),
        MyItem("Breakthrough in Cancer Research Offers New Hope", "Scientists have announced a significant breakthrough in cancer research, discovering a novel treatment that targets cancer cells more effectively. Early trials show promising results, paving the way for potentially transformative therapies."),
        MyItem("Global Economy Shows Signs of Recovery", "After a tumultuous few years, the global economy is showing signs of recovery. Economic indicators reveal a rise in consumer spending and job growth, leading analysts to predict a brighter financial outlook for 2024."),
        MyItem("Historic Peace Agreement Reached in Middle East", "Leaders from conflicting nations have come together to sign a historic peace agreement, marking a new chapter in Middle Eastern relations. The deal promises to promote stability and foster economic cooperation in the region."),
        MyItem("Innovative Education Tech Transforms Learning", "A new wave of educational technology is transforming the learning experience for students worldwide. Tools such as virtual reality and AI-driven tutoring are enhancing engagement and personalizing education like never before.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.listView)
        val fragmentContainer: FrameLayout = findViewById(R.id.fragmentContainer)

        // Initialize the first fragment to display
        if (savedInstanceState == null) {
            val initialFragment = ItemDetailFragment.newInstance(items[0]) // Show first item initially
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, initialFragment)
                .commit()
        }

        listView.adapter = MyListAdapter(items) { item ->
            // Update the existing fragment with the new item instead of hiding it
            val fragment = ItemDetailFragment.newInstance(item)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onBackPressed() {
        // Check if there are fragments in the back stack
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed() // Call super only if there's nothing in the back stack
        }
    }
}
