package com.example.submission_suitmedia.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.submission_suitmedia.R
import com.example.submission_suitmedia.databinding.ActivityFirstScreenBinding

class FirstScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFirstScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.buttonNext.setOnClickListener {
            val username = binding.editTextName.text.toString()
            if (username.isNotEmpty()) {
                val intent = Intent(this, SecondScreenActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
            } else {
                showDialog("Username cannot be empty!")
            }

        }
        binding.buttonCheck.setOnClickListener {
            val text = binding.editTextPalindrome.text.toString()
            if (text.isNotEmpty()) {
                if (isPalindrome(text)) {
                    showDialog("Hello, $text! You are a Palindrome")
                } else {
                    showDialog("Hello, $text! You are not a Palindrome")
                }
            } else {
                showDialog("Palindrome cannot be empty!")
            }
        }
    }


    private fun isPalindrome(text: String): Boolean {
        val cleanText = text.replace("[^a-zA-Z0-9]".toRegex(), "").lowercase()
        return cleanText == cleanText.reversed()
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()

    }
}