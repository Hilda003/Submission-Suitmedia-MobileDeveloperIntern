package com.example.submission_suitmedia.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.submission_suitmedia.R
import com.example.submission_suitmedia.databinding.ActivitySecondScreenBinding


class SecondScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondScreenBinding

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
        binding.buttonBack.setOnClickListener {
            finish()
        }

        val username = intent.getStringExtra("username")
        Log.d("SecondScreenActivity", "Received username: $username")
        binding.txtUsername.text = username ?: "No Name"
        binding.buttonChooseUser.setOnClickListener {
            val intent = Intent(this, ThirdScreenActivity::class.java)
            startActivityForResult(intent, EXTRA_NAME)
        }
    }
    @Deprecated("Deprecated")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EXTRA_NAME && resultCode == RESULT_OK) {
            val selectedUserName = data?.getStringExtra("selectedUserName")
            binding.txtSelectedUser.text = selectedUserName
        }
    }

    companion object {
        const val EXTRA_NAME = 1
    }
}
