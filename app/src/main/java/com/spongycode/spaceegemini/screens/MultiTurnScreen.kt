package com.spongycode.spaceegemini.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.spongycode.spaceegemini.ApiType
import com.spongycode.spaceegemini.components.ConversationArea
import com.spongycode.spaceegemini.components.TypingArea
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.navigation.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MultiTurn(viewModel: MainViewModel, navController: NavHostController) {
    Scaffold(topBar = { TopBar(name = "Multi Turn", navController = navController) }) {
        Column(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize()
                .fillMaxHeight(1f)
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                ConversationArea(viewModel, apiType = ApiType.MULTI_CHAT)
            }
            TypingArea(
                viewModel = viewModel,
                apiType = ApiType.MULTI_CHAT,
                bitmaps = null,
                launcherMultipleImages = null
            )
        }
    }
}