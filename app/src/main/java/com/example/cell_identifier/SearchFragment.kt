package com.example.cell_identifier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment

class SearchFragment:Fragment() {
    private lateinit var removeButton: Button
    private lateinit var searchBar: androidx.appcompat.widget.SearchView
    private lateinit var historyList: ArrayList<String>
    private lateinit var historyListView: ListView
    private lateinit var slAdapter: SearchListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyList = ArrayList()
        historyListView = view.findViewById(R.id.search_history)

        slAdapter = SearchListAdapter(requireActivity(), historyList)
        historyListView.adapter = slAdapter
        slAdapter.notifyDataSetChanged()

//        https://salix97.tistory.com/231
        searchBar = view.findViewById(R.id.search_bar)
        searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    historyList.add(query)
                }
                slAdapter.updateList(historyList)
                slAdapter.notifyDataSetChanged()
                searchBar.setQuery("", false)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


        removeButton = view.findViewById(R.id.button_history_reset)
        removeButton.setOnClickListener(){
//            The search history will reset.
            historyList.clear()
            slAdapter.updateList(historyList)
            slAdapter.notifyDataSetChanged()

        }
    }
}