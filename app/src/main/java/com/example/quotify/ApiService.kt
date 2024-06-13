package com.example.quotify
import retrofit2.Call
import retrofit2.http.GET
interface ApiService {
    @GET("random")
    fun getRandomQuote(): Call<Quote>
}