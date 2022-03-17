package android.example.bodegaversionone

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
     val BASE_URL = "https://fakestoreapi.com/"

    lateinit var myAdapter: MyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    private val newItemActivityRequestCode = 1

    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory((application as ItemApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Just added to get recycler view working for Room with a View
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_products)
        val adapter = ItemListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerview_products.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerview_products.layoutManager = linearLayoutManager

        getMyData()

        // observer for data changes with LiveData
        itemViewModel.allItems.observe(this, Observer {
            items -> items?.let {
                //adapter.submitList(items)
            }
        })

        // sets up button for adding new items
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewItemActivity::class.java)
            startActivityForResult(intent, newItemActivityRequestCode)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newItemActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewItemActivity.EXTRA_REPLY)?.let { reply ->
                val item = Product("", "", 0, "", 0.0, Rating(0, 0.0), reply)
                itemViewModel.insert(item)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<List<Product>?> {
            override fun onResponse(
                call: Call<List<Product>?>,
                response: Response<List<Product>?>
            ) {
                val responseBody = response.body()!!
            itemViewModel.insertAll(responseBody as ArrayList<Product>)

                myAdapter = MyAdapter(baseContext, responseBody)
                myAdapter.notifyDataSetChanged()
                recyclerview_products.adapter = myAdapter


            }

            override fun onFailure(call: Call<List<Product>?>, t: Throwable) {
                Log.d("MainActivity", "onFailure: " + t.message)
            }
        })
    }
}