package com.spongycode.spaceegemini.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.screens.AboutScreen
import com.spongycode.spaceegemini.screens.SingleTurnScreen
import com.spongycode.spaceegemini.screens.ImageChatScreen
import com.spongycode.spaceegemini.screens.MultiTurnScreen
import com.spongycode.spaceegemini.screens.SetApiScreen
import com.spongycode.spaceegemini.screens.SettingsScreen


@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun MyNavigation(
    viewModel: MainViewModel,
    startDestination: String = SingleTurnMode.route
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination)
    {
        composable(MultiTurnMode.route) {
            MultiTurnScreen(viewModel, navController)
        }
        composable(ImageMode.route) {
            ImageChatScreen(viewModel, navController)
        }
        composable(SingleTurnMode.route)
        {
            SingleTurnScreen(viewModel, navController)
        }
        composable(Settings.route) {
            SettingsScreen(navController)
        }
        composable(SetApi.route) {
            SetApiScreen(viewModel, navController)
        }
        composable(About.route) {
            AboutScreen(navController)
        }
    }
}