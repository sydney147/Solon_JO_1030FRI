package com.ucb.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class CustomDialogFragment : DialogFragment() {

    // FragmentManager to handle fragment transactions
    private var fragmentManager: FragmentManager? = null

    // Method to set FragmentManager from activity or another fragment
    fun setFragmentManager(manager: FragmentManager) {
        fragmentManager = manager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView = view.findViewById<TextView>(R.id.dialog_title)
        val messageTextView = view.findViewById<TextView>(R.id.dialog_message)
        val positiveButton = view.findViewById<Button>(R.id.positive_button)
        val negativeButton = view.findViewById<Button>(R.id.negative_button)

        titleTextView.text = "Trailblaze"
        messageTextView.text = "Will you trailblaze across the vast area of space or follow the nihility of the void which consumes all beings?"

        positiveButton.setOnClickListener {
            // Action for positive button: navigate to another fragment
            if (fragmentManager != null) {
                val anotherFragment = HomeFragment() // Replace with your actual fragment
                requireFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, anotherFragment) // Use your actual container ID
                    .addToBackStack(null) // Optional: allows user to navigate back
                    .commit()
            }
            dismiss() // Close the dialog
        }

        negativeButton.setOnClickListener {
            // Action for negative button: navigate back to HomeFragment
            requireFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()) // Navigate back to HomeFragment
                .addToBackStack(null) // Optional: allows user to navigate back to the dialog if needed
                .commit()
            dismiss() // Close the dialog
        }
    }
}
