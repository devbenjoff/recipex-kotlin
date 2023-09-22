package com.example.recipex.repository

import com.example.recipex.api.RecipesApi
import com.example.recipex.api.RetrofitInstance
import com.example.recipex.models.Recipe
import com.example.recipex.models.RecipesResponse
import com.example.recipex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class RecipesRepository @Inject constructor(
    private val api: RecipesApi
) {

    suspend fun getRandomRecipes(tags: String): Resource<RecipesResponse> {
        val response = try {
            api.getRandomRecipes(tags = tags)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred.")
        }
        return Resource.Success(response)
    }

    suspend fun getRecipeInformation(recipeId: String): Resource<Recipe> {
        val response = try {
            api.getRecipeInformation(recipeId)
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "An unknown error occurred.")
        }

        return Resource.Success(response)
    }
}