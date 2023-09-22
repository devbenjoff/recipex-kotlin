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

     val _randomRecipesList = mutableStateListOf<Recipe>()
    val randomRecipesList: List<Recipe> = _randomRecipesList
    val errorMessage = mutableStateOf("")
    private var selectedTags = mutableStateListOf<String>()

    init {
        loadRandomRecipes()
    }

    fun loadRandomRecipes() {
        viewModelScope.launch {
            val tagsList: List<String> = selectedTags
            val tagsString: String = tagsList.joinToString(",")

            when (val result = repository.getRandomRecipes(tagsString)) {
                is Resource.Success -> {
                    _randomRecipesList.clear()
                    _randomRecipesList.addAll(result.data!!.recipes)
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun setTag(tag: String) {
        if (!selectedTags.contains(tag)) {
            selectedTags.add(tag)
        }
    }

    fun removeTag(tag: String) {
        if (selectedTags.contains(tag)) {
            selectedTags.remove(tag)
        }
    }
}