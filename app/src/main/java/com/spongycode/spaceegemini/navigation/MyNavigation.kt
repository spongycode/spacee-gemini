package com.spongycode.spaceegemini.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.screens.AboutScreen
import com.spongycode.spaceegemini.screens.HomeScreen
import com.spongycode.spaceegemini.screens.ImageText
import com.spongycode.spaceegemini.screens.MultiTurn
import com.spongycode.spaceegemini.screens.SetApiScreen
import com.spongycode.spaceegemini.screens.SettingsScreen


@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun MyNavigation(
    viewModel: MainViewModel,
    startDestination: String = Home.route
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination)
    {
        composable(Home.route)
        {
            HomeScreen(viewModel, navController)
        }
        composable(MultiTurn.route) {
            MultiTurn(viewModel, navController)
        }
        composable(ImageText.route) {
            ImageText(viewModel, navController)
        }
        composable(Settings.route) {
            SettingsScreen(viewModel, navController)
        }
        composable(About.route) {
            AboutScreen(navController)
        }
    }
}