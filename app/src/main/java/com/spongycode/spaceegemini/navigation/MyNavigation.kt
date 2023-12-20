package com.spongycode.spaceegemini.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spongycode.spaceegemini.MainViewModel
import com.spongycode.spaceegemini.screens.AboutScreen
import com.spongycode.spaceegemini.screens.HomeScreen

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun MyNavigation(
    viewModel: MainViewModel,
    onSearchClicked: (String) -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home.route)
    {
        composable(Home.route)
        {
            HomeScreen(viewModel, onSearchClicked, navController)
        }
        composable(About.route){
            AboutScreen(navController)
        }
    }
}