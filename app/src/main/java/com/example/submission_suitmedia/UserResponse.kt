package com.example.submission_suitmedia

import com.example.submission_suitmedia.data.User


data class UserResponse(
    val page: Int,
    val perPage: Int,
    val total: Int,
    val totalPages: Int,
    val data: List<User>
)
