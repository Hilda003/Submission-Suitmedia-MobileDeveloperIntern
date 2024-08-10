package com.example.submission_suitmedia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission_suitmedia.adapter.UserAdapter
import com.example.submission_suitmedia.data.DataItem
import com.example.submission_suitmedia.data.Response
import com.example.submission_suitmedia.databinding.ActivitySecondScreenBinding
import retrofit2.Call
import retrofit2.Callback


class SecondScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondScreenBinding
    private val userList = mutableListOf<DataItem>()
    private var currentPage = 1
    private var totalPages = 1
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        loadUsers(currentPage)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        val username = intent.getStringExtra("username")
        Log.d("SecondScreenActivity", "Received name: $username")
        binding.txtUsername.text = username ?: "No Name"

        binding.buttonChooseUser.setOnClickListener {
            toggleRecyclerViewVisibility()
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

    private fun toggleRecyclerViewVisibility() {
        val isVisible = binding.recyclerView.visibility == View.VISIBLE
        binding.recyclerView.visibility = if (isVisible) View.GONE else View.VISIBLE

        // Toggle visibility of other views
        binding.txtUsername.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.txtWelcome.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.txtSelectedUser.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.buttonChooseUser.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(userList) { user ->
            val selectedUserName = "${user.firstName} ${user.lastName}"
            binding.txtSelectedUser.text = selectedUserName
            toggleRecyclerViewVisibility() // Hide RecyclerView and show other views
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SecondScreenActivity)
            adapter = userAdapter
        }
    }

    private fun loadUsers(page: Int) {
        val apiService = ApiService.create()
        apiService.getUsers(page, 10).enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        userList.addAll(it.data)
                        userAdapter.notifyDataSetChanged()
                        totalPages = it.totalPages
                    }
                } else {
                    Toast.makeText(this@SecondScreenActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toast.makeText(this@SecondScreenActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        const val EXTRA_NAME = 1
    }
}
