package com.example.submission_suitmedia.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission_suitmedia.adapter.UserAdapter
import com.example.submission_suitmedia.data.ApiService
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
        binding.swipeRefreshLayout.isRefreshing = true

        val apiService = ApiService.create()
        apiService.getUsers(page, 10).enqueue(object : Callback<Response> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                binding.swipeRefreshLayout.isRefreshing = false

                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.data.isEmpty() && page == 1) {
                            showEmptyState()
                        } else {
                            hideEmptyState()
                            userList.addAll(it.data)
                            userAdapter.notifyDataSetChanged()
                            totalPages = it.totalPages
                        }
                    }
                } else {
                    Toast.makeText(this@ThirdScreenActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                    if (userList.isEmpty()) {
                        showEmptyState()
                    }
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                binding.swipeRefreshLayout.isRefreshing = false

                Toast.makeText(this@ThirdScreenActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                if (userList.isEmpty()) {
                    showEmptyState()
                }
            }
        })
    }


    private fun showEmptyState() {
        binding.recyclerView.visibility = View.GONE
        binding.emptyStateLayout.visibility = View.VISIBLE
    }

    private fun hideEmptyState() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.emptyStateLayout.visibility = View.GONE
    }
}