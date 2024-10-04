package com.example.bottomnavigation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlin.random.Random // Import Random

data class TodoItem(val id: Int, val title: String, val isCompleted: Boolean, val imageResId: Int)

class ListFragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var taskTitleEditText: EditText
    private lateinit var addTaskButton: Button
    private val todos = mutableListOf<TodoItem>()
    private lateinit var todoAdapter: TodoAdapter

    // Array of image resources to randomize
    private val imageResources = arrayOf(
        R.drawable.acheron,
        R.drawable.aventurine,
        R.drawable.guinaifen,
        R.drawable.robin,
        R.drawable.blackswan
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the fragment_list.xml layout
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Referencing views with correct IDs as per fragment_list.xml
        listView = view.findViewById(R.id.customListView) // Using customListView ID
        taskTitleEditText = view.findViewById(R.id.editText)
        addTaskButton = view.findViewById(R.id.addBtn)

        // Set up the adapter and attach it to the ListView
        todoAdapter = TodoAdapter(requireContext(), todos)
        listView.adapter = todoAdapter

        // Add button click listener
        addTaskButton.setOnClickListener {
            val title = taskTitleEditText.text.toString()
            if (title.isNotBlank()) {
                addTask(title)
                taskTitleEditText.text.clear()
            }
        }

        return view
    }

    // Adds a new task with a random image from the array
    private fun addTask(title: String) {
        val newId = todos.maxOfOrNull { it.id }?.plus(1) ?: 1

        // Select a random image resource
        val randomImageResId = imageResources[Random.nextInt(imageResources.size)]

        val newTask = TodoItem(newId, title, false, randomImageResId)
        todos.add(newTask)
        todoAdapter.notifyDataSetChanged() // Refresh the list
    }

    // Inner class to handle the ListView's adapter
    private inner class TodoAdapter(private val context: Context, private val todos: MutableList<TodoItem>) : BaseAdapter() {
        private val inflater: LayoutInflater = LayoutInflater.from(context)
        private val doubleTapDelay: Long = 300
        private var lastClickTime: Long = 0
        private var lastPosition: Int = -1

        override fun getCount(): Int = todos.size

        override fun getItem(position: Int): Any = todos[position]

        override fun getItemId(position: Int): Long = position.toLong()

        @SuppressLint("ClickableViewAccessibility")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(R.layout.item_todo, parent, false)
            val todoItem = getItem(position) as TodoItem

            // Correctly referencing views in item_todo.xml
            val imageView = view.findViewById<ImageView>(R.id.imageView)
            val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
            val titleTextView = view.findViewById<TextView>(R.id.textView)

            // Bind the TodoItem's data to the views
            imageView.setImageResource(todoItem.imageResId)
            checkBox.isChecked = todoItem.isCompleted
            titleTextView.text = todoItem.title

            // Set up the double-tap listener for the entire row
            view.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val clickTime = System.currentTimeMillis()
                        if (clickTime - lastClickTime < doubleTapDelay && lastPosition == position) {
                            showEditOrDeleteDialog(position)
                        }
                        lastClickTime = clickTime
                        lastPosition = position
                    }
                }
                true
            }

            return view
        }

        // Show options dialog for Edit or Delete
        private fun showEditOrDeleteDialog(position: Int) {
            AlertDialog.Builder(context)
                .setTitle("Choose an option")
                .setItems(arrayOf("Edit", "Delete")) { _, which ->
                    when (which) {
                        0 -> showEditDialog(position)
                        1 -> deleteTask(position)
                    }
                }
                .show()
        }

        // Show dialog to edit an item
        private fun showEditDialog(position: Int) {
            val todoItem = getItem(position) as TodoItem
            val editText = EditText(context).apply {
                setText(todoItem.title)
            }

            AlertDialog.Builder(context)
                .setTitle("Edit Task")
                .setView(editText)
                .setPositiveButton("Save") { _, _ ->
                    val newTitle = editText.text.toString()
                    if (newTitle.isNotBlank()) {
                        todos[position] = todoItem.copy(title = newTitle)
                        notifyDataSetChanged()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // Delete a task
        private fun deleteTask(position: Int) {
            todos.removeAt(position)
            notifyDataSetChanged() // Refresh the list
        }
    }
}
