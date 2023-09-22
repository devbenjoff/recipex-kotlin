package com.example.recipex.recipedetail

import androidx.lifecycle.ViewModel
import com.example.recipex.models.Recipe
import com.example.recipex.repository.RecipesRepository
import com.example.recipex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: RecipesRepository
) : ViewModel() {

    suspend fun getRecipeInformation(recipeId: String): Resource<Recipe> =
        repository.getRecipeInformation(recipeId = recipeId)
}