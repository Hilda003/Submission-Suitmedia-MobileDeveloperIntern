package com.example.submission_suitmedia.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission_suitmedia.R
import com.example.submission_suitmedia.data.ApiService
import com.example.submission_suitmedia.adapter.UserAdapter
import com.example.submission_suitmedia.data.DataItem
import com.example.submission_suitmedia.data.Response
import com.example.submission_suitmedia.databinding.ActivityThirdScreenBinding
import retrofit2.Call
import retrofit2.Callback

class ThirdScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThirdScreenBinding
    private val userList = mutableListOf<DataItem>()
    private var currentPage = 1
    private var totalPages = 1
    private lateinit var userAdapter: UserAdapter
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        setupSwipeRefresh()
        loadUsers(currentPage)

        binding.buttonBack.setOnClickListener {
            finish()
        }
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
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && currentPage < totalPages
                    ) {
                        loadUsers(++currentPage)
                    }
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            userList.clear()
            userAdapter.notifyDataSetChanged()
            loadUsers(currentPage)
        }
    }

    private fun loadUsers(page: Int) {
        isLoading = true
        binding.swipeRefreshLayout.isRefreshing = true
        val apiService = ApiService.create()
        apiService.getUsers(page, 10).enqueue(object : Callback<Response> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                isLoading = false
                binding.swipeRefreshLayout.isRefreshing = false
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
                isLoading = false
                binding.swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@ThirdScreenActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}