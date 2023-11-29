package com.example.cell_identifier

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cell_identifier.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment:Fragment() {
    private lateinit var historyList: ArrayList<String>
    private lateinit var historyListView: ListView
    private lateinit var slAdapter: SearchListAdapter
    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dbRef = FirebaseDatabase.getInstance().getReference(Globals.DB_HISTORY)
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        updateHistory()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyList = ArrayList()
        historyListView = view.findViewById(R.id.search_history)

        slAdapter = SearchListAdapter(requireActivity(), historyList)
        historyListView.adapter = slAdapter
        slAdapter.notifyDataSetChanged()

//        https://salix97.tistory.com/231
        binding.searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val keyword = SearchHistory(query)
                    dbRef.push().setValue(keyword)
                }
                binding.searchBar.setQuery("", false)
                search(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

//        Clear SearchHistory in Firebase RT database
        binding.buttonHistoryReset.setOnClickListener{
            historyList.clear()
            dbRef.removeValue().addOnSuccessListener {
                Toast.makeText(requireActivity(), "History Cleared", Toast.LENGTH_LONG).show()
            }
            slAdapter.updateList(historyList)
            slAdapter.notifyDataSetChanged()

        }
    }

//    This function will retrieve the history of queries from firebase RT database and display
    private fun updateHistory() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                historyList.clear()
                if (snapshots.exists()) {
                    for (snapshot in snapshots.children) {
                        val query = snapshot.getValue(SearchHistory::class.java)
                        if(query != null && query.keyword != "") {
                            historyList.add(query.keyword)
                        }
                    }
                }
//                Reverse the historyList before passing so that the newest search is shown on the top
                val historyAdapter = SearchListAdapter(requireActivity(), historyList.reversed())
                historyListView.adapter = historyAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

//    OPen a SearchResult activity to display the list of slides matching the query
    private fun search(keyword:String){
            val intent = Intent(context, SearchResult::class.java)
            intent.putExtra(Globals.INTENT_SEARCH_KEY, keyword)
            this.startActivity(intent)
    }

//    Data class for search history
    data class SearchHistory(
        var keyword: String = ""
    )
}