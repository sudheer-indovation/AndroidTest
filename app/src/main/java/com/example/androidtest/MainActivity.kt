package com.example.androidtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.NumberPicker
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
import androidx.core.view.MenuItemCompat.getActionView
import android.view.MenuInflater
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler
import android.widget.Button


class MainActivity : AppCompatActivity(), RecyclerAdapter.OnSelect {
    override fun onSelect(b: Boolean) {
        if (b) {
            selectionCount++;
            notifCount.setText("" + selectionCount)
        } else {
            selectionCount--;
            notifCount.setText("" + selectionCount)
        }
    }

    lateinit var layoutManager: LinearLayoutManager
    var list = ArrayList<DataListModel>()
    lateinit var adapter: RecyclerAdapter;
    var isLoadingData = false
    var nPages = 20
    lateinit var notifCount: Button
    var selectionCount = 0
    private lateinit var mRunnable: Runnable
    private lateinit var mHandler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layoutManager = LinearLayoutManager(this);
        recyclerView.layoutManager = layoutManager;
        adapter = RecyclerAdapter(list, this, this)
        recyclerView.adapter = adapter
        swipe_refresh_layout.setOnRefreshListener {
            getData(1)
        }
        getData(1)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (!isLoadingData && totalItemCount == lastVisibleItemPosition + 1) {
                    getData((list.size / nPages) + 1)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_count, menu)

//        val count = menu!!.findItem(R.id.badge).actionView
//        notifCount = count.findViewById(R.id.notif_count) as Button
//        notifCount.setText("" + selectionCount)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val count = menu!!.findItem(R.id.badge)
        count.setActionView(R.layout.button)
        notifCount = count.actionView.findViewById(R.id.notif_count) as Button
        notifCount.setText("" + selectionCount)
        return super.onPrepareOptionsMenu(menu)

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
                swipe_refresh_layout.isRefreshing = false
                try {
                    var data = JSONObject(response)
                    nPages = data.getInt("hitsPerPage")
                    var array = data.getJSONArray("hits")
                    if (pageNumber == 1) {
                        list.clear();
                        adapter.notifyDataSetChanged()
                        selectionCount=0;
                        notifCount.setText("" + selectionCount)
                    }
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
                    swipe_refresh_layout.isRefreshing = false
                }
            },
            Response.ErrorListener {
                swipe_refresh_layout.isRefreshing = false
            })

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}
