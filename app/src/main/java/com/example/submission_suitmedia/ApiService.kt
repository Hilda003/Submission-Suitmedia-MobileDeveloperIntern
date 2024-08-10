package com.example.submission_suitmedia

import com.example.submission_suitmedia.data.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    fun getUsers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<Response>

    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}