package com.example.quotify

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var quoteTextView: MaterialTextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quoteTextView = findViewById(R.id.quoteTextView)
        val refreshButton: MaterialButton = findViewById(R.id.refreshButton)
        val shareButton: MaterialButton = findViewById(R.id.shareButton)
        val favoriteButton: MaterialButton = findViewById(R.id.favoriteButton)
        val viewFavoritesButton: MaterialButton = findViewById(R.id.viewFavoritesButton)

        apiService = ApiClient.retrofit.create(ApiService::class.java)
        sharedPreferences = getSharedPreferences("quotes", MODE_PRIVATE)

        fetchDailyQuote()

        refreshButton.setOnClickListener { fetchDailyQuote() }

        shareButton.setOnClickListener { shareQuote() }

        favoriteButton.setOnClickListener { saveFavoriteQuote() }

        viewFavoritesButton.setOnClickListener { viewFavorites() }
    }

    private fun fetchDailyQuote() {
        apiService.getRandomQuote().enqueue(object : Callback<Quote> {
            override fun onResponse(call: Call<Quote>, response: Response<Quote>) {
                if (response.isSuccessful && response.body() != null) {
                    quoteTextView.text = response.body()?.content
                } else {
                    quoteTextView.text = "Failed to fetch quote"
                }
            }

            override fun onFailure(call: Call<Quote>, t: Throwable) {
                quoteTextView.text = "Failed to fetch quote"
            }
        })
    }

    private fun shareQuote() {
        val quote = quoteTextView.text.toString()
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, quote)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Share quote via"))
    }

    private fun saveFavoriteQuote() {
        val quote = quoteTextView.text.toString()
        val editor = sharedPreferences.edit()
        var favorites = sharedPreferences.getString("favorites", "") ?: ""
        favorites += "$quote;;"
        editor.putString("favorites", favorites)
        editor.apply()
        Toast.makeText(this, "Quote saved to favorites", Toast.LENGTH_SHORT).show()
    }

    private fun viewFavorites() {
        val intent = Intent(this, FavoritesActivity::class.java)
        intent.putExtra("favorites", sharedPreferences.getString("favorites", ""))
        startActivity(intent)
    }
}