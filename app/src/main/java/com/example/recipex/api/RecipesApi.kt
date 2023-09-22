package com.example.recipex.api

import com.example.recipex.models.Recipe
import com.example.recipex.models.RecipesResponse
import com.example.recipex.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipesApi {

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number")
        number: Int = 20,
        @Query("tags")
        tags: String,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): RecipesResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id")
        id: String,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Recipe
}