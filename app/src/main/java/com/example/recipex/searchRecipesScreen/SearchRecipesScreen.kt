package com.example.recipex.searchRecipesScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRecipesScreen() {
    Scaffold() { innerPadding ->
       Text(text = "Search")
    }
}