package com.example.recipex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipex.homescreen.HomeScreen
import com.example.recipex.homescreen.HomeScreenViewModel
import com.example.recipex.models.Screen
import com.example.recipex.recipedetail.RecipeDetailScreen
import com.example.recipex.repository.RecipesRepository
import com.example.recipex.searchRecipesScreen.SearchRecipesScreen
import com.example.recipex.ui.theme.RecipexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint()
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = listOf(
            Screen.Home,
            Screen.SearchRecipes,
        )

        setContent {
            RecipexTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                BottomNavigationItem(
                                    icon = { Icon(screen.icon!!, contentDescription = null) },
                                    label = { Text(stringResource(screen.resourceId)) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = Screen.Home.route,
                        Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) { HomeScreen(navController) }
                        composable(Screen.SearchRecipes.route) { SearchRecipesScreen() }
                        composable(
                            "${Screen.RecipeDetails.route}/{recipeId}",
                            arguments = listOf(
                                navArgument("recipeId") {
                                    type = NavType.StringType
                                }
                            ),
                        ) {navBackStackEntry ->
                            val id = navBackStackEntry.arguments?.getString("recipeId")
                            RecipeDetailScreen(recipeId = id ?: "")
                        }
                    }
                }
            }
        }
    }
}





