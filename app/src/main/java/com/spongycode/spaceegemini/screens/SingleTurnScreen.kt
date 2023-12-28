package com.spongycode.spaceegemini.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.spongycode.spaceegemini.ApiType
import com.spongycode.spaceegemini.R
import com.spongycode.spaceegemini.components.ConversationArea
import com.spongycode.spaceegemini.components.TypingArea
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.navigation.TopBar

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SingleTurnScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    Scaffold(topBar = {
        TopBar(
            name = stringResource(id = R.string.single_turn_mode),
            navController = navController
        )
    }) {
        Column(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize()
                .fillMaxHeight(1f)
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                ConversationArea(viewModel, apiType = ApiType.SINGLE_CHAT)
            }
            TypingArea(
                viewModel = viewModel,
                apiType = ApiType.SINGLE_CHAT,
                bitmaps = null,
                galleryLauncher = null,
                permissionLauncher = null
            )
        }
    }

}

