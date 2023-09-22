package com.example.recipex.homescreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.recipex.R.string.*
import com.example.recipex.appbar.RecipexAppBar
import com.example.recipex.models.Recipe
import com.example.recipex.models.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val recipes = homeScreenViewModel.randomRecipesList
    val errorMessage = homeScreenViewModel.errorMessage

    Scaffold(
        topBar = {
            RecipexAppBar()
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                AppTextAndDescription()
                Spacer(modifier = Modifier.height(20.dp))
                FoodCountriesList()
                Spacer(modifier = Modifier.height(50.dp))
                RecipesList(recipes = recipes, navController)
                Text(text = errorMessage.value)
            }
        }
    }
}

@Composable
fun FoodCountriesList() {
    val listOfItems = listOf<String>(
        "Mexican", "Indian", "Bosnian", "American", "Chinese"
    )

    LazyRow() {
        items(listOfItems.size) { index ->
            FoodCountryCard(title = listOfItems[index], index)
        }
    }
}

@Composable
fun FoodCountryCard(title: String, index: Int) {
    var selected by rememberSaveable { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Card(modifier = Modifier
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            selected = !selected
        }
        .padding(end = 10.dp, start = if (index == 0) 10.dp else 0.dp)
        .border(BorderStroke(0.5.dp, Color(255, 114, 76)), RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Color(255, 114, 76) else Color.Black,
        ),
        shape = RoundedCornerShape(10.dp)

    ) {
        Text(
            text = title, style = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            ), modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun AppTextAndDescription() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Recipex", style = TextStyle(
                fontSize = 40.sp, fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Find thousands of popular recipes", style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Light,
                color = Color.LightGray
            )
        )
    }
}

@Composable
fun RecipesList(recipes: List<Recipe>, navController: NavController) {
    Column {
        Text(
            text = "Today's Recommended",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White),
        )
        Spacer(modifier = Modifier.height(10.dp))

        LazyRow() {
            items(recipes.size, key = {
                recipes[it].id
            }) { index ->
                RecipeCard(recipe = recipes[index], navController)
                Spacer(modifier = Modifier.width(30.dp))
            }
        }
    }

}

@Composable
fun RecipeCard(recipe: Recipe, navController: NavController) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .clickable {
                navController.navigate(
                    "${Screen.RecipeDetails.route}/${recipe.id}"
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .width(200.dp)
                .background(Color.White)
        ) {
            AsyncImage(
                model = recipe.image,
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = recipe.title,
            style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}