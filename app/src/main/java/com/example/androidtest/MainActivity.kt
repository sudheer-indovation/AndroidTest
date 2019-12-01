package com.example.androidtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.androidtest.adapter.RecyclerAdapter
import com.example.androidtest.model.DataListModel
import com.example.androidtest.model.DataModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity(),RecyclerAdapter.OnSelect {
    override fun onSelect(b: Boolean) {
        
    }

    lateinit var layoutManager: LinearLayoutManager
    var list = ArrayList<DataListModel>()
    lateinit var adapter: RecyclerAdapter;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layoutManager = LinearLayoutManager(this);
        recyclerView.layoutManager = layoutManager;
        adapter = RecyclerAdapter(list, this,this)
        recyclerView.adapter = adapter

        getData(1)
    }

    fun getData(pageNumber: Int) {
// ...

// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://hn.algolia.com/api/v1/search_by_date?tags=story&page=" + pageNumber

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Log.i("response", response);
                try {
                    var data = JSONObject(response)
                    var array = data.getJSONArray("hits")
                    for (i in 0 until array.length()) {
                        var dataListModel = DataListModel()
                        var itemData = JSONObject(array[i].toString())
                        dataListModel.created_at = itemData.getString("created_at")
                        dataListModel.title = itemData.getString("title")
                        list.add(dataListModel)
                    }

                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { })

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}
