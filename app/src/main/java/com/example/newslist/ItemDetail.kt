package com.example.newslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ItemDetailFragment : Fragment() {

    private lateinit var item: MyItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = it.getParcelable("item")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_detail, container, false)

        // Set the title and description in the respective TextViews
        view.findViewById<TextView>(R.id.itemTitle).text = item.title
        view.findViewById<TextView>(R.id.itemDescription).text = item.description

        return view
    }

    companion object {
        fun newInstance(item: MyItem) =
            ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("item", item)
                }
            }
    }
}
