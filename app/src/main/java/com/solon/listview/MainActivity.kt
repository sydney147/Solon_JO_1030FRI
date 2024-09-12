package com.solon.listview

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var itemList: ArrayList<ListItem>
    private lateinit var listView: ListView
    private lateinit var adapter: CustomListAdapter

    // Array of drawable resources for random selection
    private val imageResources = arrayOf(
        R.drawable.acheron,
        R.drawable.aventurine,
        R.drawable.blackswan,
        R.drawable.guinaifen,
        R.drawable.robin
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        val editText = findViewById<EditText>(R.id.editText)
        val addButton = findViewById<Button>(R.id.addBtn)
        listView = findViewById(R.id.customListView)

        // Initialize list and adapter
        itemList = ArrayList()
        adapter = CustomListAdapter()
        listView.adapter = adapter

        // Add button click event
        addButton.setOnClickListener {
            val newItemText = editText.text.toString()
            if (newItemText.isNotEmpty()) {
                // Select a random image from the array
                val randomImage = imageResources[Random.nextInt(imageResources.size)]

                // Create a new ListItem with the random image
                val newItem = ListItem(newItemText, randomImage)
                itemList.add(newItem)
                adapter.notifyDataSetChanged() // Refresh the list
                editText.text.clear()
            }
        }

        // Set item click listener for showing options
        listView.setOnItemClickListener { _, _, position, _ ->
            showEditDeleteDialog(position)
        }
    }

    // Show dialog for edit or delete actions
    private fun showEditDeleteDialog(position: Int) {
        val options = arrayOf("Edit", "Delete")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an action")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> editItem(position)
                1 -> deleteItem(position)
            }
        }
        builder.show()
    }

    // Edit the selected item
    private fun editItem(position: Int) {
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        input.setText(itemList[position].text)
        builder.setTitle("Edit Item")
        builder.setView(input)
        builder.setPositiveButton("Update") { _, _ ->
            itemList[position].text = input.text.toString()
            adapter.notifyDataSetChanged() // Refresh the list
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    // Delete the selected item
    private fun deleteItem(position: Int) {
        itemList.removeAt(position)
        adapter.notifyDataSetChanged() // Refresh the list
    }

    // Data model for each list item
    data class ListItem(
        var text: String,
        var imageResId: Int
    )

    // Custom adapter class (now inside MainActivity)
    inner class CustomListAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return itemList.size
        }

        override fun getItem(position: Int): Any {
            return itemList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater = LayoutInflater.from(this@MainActivity)
            val rowView = convertView ?: inflater.inflate(R.layout.list_view, parent, false)

            // Get references to views
            val textView = rowView.findViewById<TextView>(R.id.textView)
            val imageView = rowView.findViewById<ImageView>(R.id.imageView)

            // Get the current item
            val currentItem = itemList[position]

            // Bind data to the views
            textView.text = currentItem.text
            imageView.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, currentItem.imageResId))

            // Set click listeners for the TextView and ImageView to trigger the dialog
            textView.setOnClickListener {
                listView.performItemClick(rowView, position, getItemId(position))
            }

            imageView.setOnClickListener {
                listView.performItemClick(rowView, position, getItemId(position))
            }

            return rowView
        }
    }
}
