package com.example.cell_identifier

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SearchListAdapter(private val context: Context, private var historyList:List<String>):BaseAdapter() {


    override fun getCount(): Int {
        return historyList.count()
    }

    override fun getItem(position: Int): Any {
        return historyList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(context, R.layout.search_history_list_adapter, null)

        val resultView = view.findViewById<TextView>(R.id.search_history_item)
        resultView.text = historyList[position]

//            Make each element in search history clickable
//            Open a result activity - list of slides matching with the query
        view.setOnClickListener(){
            val intent = Intent(context, SearchResult::class.java)
            intent.putExtra(Globals.INTENT_SEARCH_KEY, historyList[position])
            context.startActivity(intent)
        }

        return view
    }

    fun updateList(newList:List<String>){
        historyList = newList
    }
}