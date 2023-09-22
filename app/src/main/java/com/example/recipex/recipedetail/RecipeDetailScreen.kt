package com.example.recipex.recipedetail

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import com.example.recipex.R
import com.example.recipex.models.ExtendedIngredient
import com.example.recipex.models.Ingredient
import com.example.recipex.models.Recipe
import com.example.recipex.ui.theme.orange
import com.example.recipex.ui.theme.playfairFontFamily
import com.example.recipex.util.Resource
import com.example.recipex.util.toAnnotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: String,
    viewModel: RecipeDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val recipe = produceState<Resource<Recipe>>(initialValue = Resource.Loading()) {
        value = viewModel.getRecipeInformation(recipeId)
    }.value

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            RecipeDetailStateWrapper(recipeInfo = recipe, navController = navController)
        }
    }
}

@Composable
fun RecipeDetailStateWrapper(
    recipeInfo: Resource<Recipe>,
    navController: NavController
) {
    when (recipeInfo) {
        is Resource.Success -> {
            RecipeDetailCore(recipe = recipeInfo.data!!, navController = navController)
        }

        is Resource.Error -> {
            Text(recipeInfo.message!!)
        }

        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun RecipeDetailCore(recipe: Recipe, navController: NavController) {
    val spannedText = HtmlCompat.fromHtml(recipe.summary, HtmlCompat.FROM_HTML_MODE_COMPACT)
    Column {
        RecipeImageAndInterestingStuff(recipe = recipe, navController)
        Spacer(modifier = Modifier.height(10.dp))
        Column(
        ) {
            Text(
                text = spannedText.toAnnotatedString(),
                style = TextStyle(fontSize = 13.sp, fontFamily = playfairFontFamily),
                modifier = Modifier.padding(20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Ingredients",
                style = TextStyle(
                    fontFamily = playfairFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            IngredientsList(recipe = recipe)
        }
    }
}

@Composable
fun IngredientsList(recipe: Recipe) {
    LazyRow() {
        items(recipe.extendedIngredients.size) { index ->
            IngredientItem(ingredient = recipe.extendedIngredients[index], index)
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}

@Composable
fun IngredientItem(ingredient: ExtendedIngredient, index: Int) {
    Box(
        modifier = Modifier.padding(start = if (index == 0) 20.dp else 0.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier.padding(3.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = ingredient.name,
                    style = TextStyle(
                        color = Color.Black, fontFamily = playfairFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "${ingredient.measures.metric.amount} ${ingredient.measures.metric.unitShort}",
                    style = TextStyle(color = orange, fontFamily = playfairFontFamily)
                )
            }
        }
        Box(
            modifier = Modifier
                .size(25.dp)
                .padding(1.dp)
                .clip(CircleShape)
                .background(orange)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = index.toString(),
                color = Color.White,
                fontFamily = playfairFontFamily
            )
        }
    }
}

@Composable
fun RecipeImageAndInterestingStuff(recipe: Recipe, navController: NavController) {
    Box(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .height(230.dp)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp, topEnd = 0.dp, bottomEnd = 20.dp, bottomStart = 20.dp
                    )
                )
                .background(Color.Blue)
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1480&q=80",
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }
        Box(modifier = Modifier.padding(20.dp)) {
            BackButton(navController)
        }
        RecipeInterestingStuff(
            recipe = recipe, modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun BackButton(navController: NavController) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .border(1.dp, Color.White, RoundedCornerShape(5.dp))
            .clickable {
                navController.popBackStack()
            }
    ) {
        Icon(
            Icons.Filled.ArrowBack,
            null,
            tint = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun RecipeInterestingStuff(recipe: Recipe, modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(30.dp)
                )
                .background(Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = recipe.title, style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontFamily = playfairFontFamily
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconText(
                    text = "Ready in ${recipe.readyInMinutes} min",
                    icon = Icons.Filled.Refresh
                )
                IconText(
                    text = "$${recipe.pricePerServing} per serving",
                    icon = Icons.Filled.ShoppingCart
                )
                IconText(text = recipe.aggregateLikes.toString(), icon = Icons.Filled.Favorite)
            }
        }
    }
}

@Composable
fun IconText(text: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 5.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Black)
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = orange,
                fontSize = 13.sp,
                fontFamily = playfairFontFamily,
            )
        )
    }
}

