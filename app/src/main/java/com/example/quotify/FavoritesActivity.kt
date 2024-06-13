package com.example.quotify

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoritesListView: ListView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favoritesListView = findViewById(R.id.favoritesListView)
        sharedPreferences = getSharedPreferences("quotes", MODE_PRIVATE)

        loadFavorites()

    }

    private fun loadFavorites() {
        val favorites = sharedPreferences.getString("favorites", "") ?: ""
        val favoriteList = favorites.split(";;").filter { it.isNotEmpty() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, favoriteList)
        favoritesListView.adapter = adapter
    }
}
