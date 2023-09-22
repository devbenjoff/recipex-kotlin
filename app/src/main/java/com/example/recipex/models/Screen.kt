package com.example.recipex.models

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.recipex.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector?) {
    object Home : Screen("home", R.string.home, Icons.Filled.Home)
    object SearchRecipes : Screen("search_recipes", R.string.search_recipes, Icons.Filled.Search)
    object RecipeDetails: Screen("recipe_details", R.string.recipe_details , null)
}