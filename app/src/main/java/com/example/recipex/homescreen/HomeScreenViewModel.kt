package com.example.recipex.homescreen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipex.models.Recipe
import com.example.recipex.repository.RecipesRepository
import com.example.recipex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: RecipesRepository
) : ViewModel() {

    private val _randomRecipesList = mutableStateListOf<Recipe>()
    val randomRecipesList: List<Recipe> = _randomRecipesList
    val errorMessage = mutableStateOf("")

    init {
        loadRandomRecipes()
    }

    private fun loadRandomRecipes() {
        viewModelScope.launch {
            when (val result = repository.getRandomRecipes()) {
                is Resource.Success -> {
                    _randomRecipesList.addAll(result.data!!.recipes)
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                }
                is Resource.Loading -> {}
            }
        }
    }
}