package com.example.submission_suitmedia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission_suitmedia.adapter.UserAdapter
import com.example.submission_suitmedia.data.DataItem
import com.example.submission_suitmedia.data.Response
import com.example.submission_suitmedia.databinding.ActivityThirdScreenBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response as RetrofitResponse

class ThirdScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdScreenBinding
    private val userList = mutableListOf<DataItem>()
    private var currentPage = 1
    private var totalPages = 1
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadUsers(currentPage)


        binding.buttonBack.setOnClickListener {
            finish()
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPage < totalPages) {
                        loadUsers(++currentPage)
                    }
                }
            }
        })
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(userList) { user ->
            val intent = Intent().apply {
                putExtra("selectedUserName", "${user.firstName} ${user.lastName}")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ThirdScreenActivity)
            adapter = userAdapter
        }
    }


    private fun loadUsers(page: Int) {
//        binding.swipeRefreshLayout.isRefreshing = true

        val apiService = ApiService.create()
        apiService.getUsers(page, 10).enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: RetrofitResponse<Response>) {
//                binding.swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        userList.addAll(it.data)
                        userAdapter.notifyDataSetChanged()
                        totalPages = it.totalPages
                    }
                } else {
                    Toast.makeText(this@ThirdScreenActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
//                binding.swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@ThirdScreenActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
