package com.spongycode.spaceegemini.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.request.ImageRequest
import com.spongycode.spaceegemini.ApiType
import com.spongycode.spaceegemini.components.ConversationArea
import com.spongycode.spaceegemini.components.SelectedImageArea
import com.spongycode.spaceegemini.components.TypingArea
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.navigation.TopBar
import com.spongycode.util.ImageHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ImageChatScreen(viewModel: MainViewModel, navController: NavHostController) {

    val bitmaps: SnapshotStateList<Bitmap> = remember {
        mutableStateListOf()
    }
    val context = LocalContext.current

    val imageRequestBuilder = ImageRequest.Builder(context)
    val imageLoader = ImageLoader.Builder(context).build()

    val coroutineScope = rememberCoroutineScope()

    val launcherMultipleImages = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
    ) {
        it.forEach { uri ->
            coroutineScope.launch {
                ImageHelper.scaleDownBitmap(uri, imageRequestBuilder, imageLoader)?.let { bitmap ->
                    bitmaps.add(bitmap)
                }
            }
        }
    }

    Scaffold(topBar = { TopBar(name = "Image Chat", navController = navController) }) {
        Column(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize()
                .fillMaxHeight(1f)
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                ConversationArea(viewModel, apiType = ApiType.IMAGE_CHAT)
            }
            SelectedImageArea(bitmaps = bitmaps)
            TypingArea(
                viewModel = viewModel,
                apiType = ApiType.IMAGE_CHAT,
                bitmaps = bitmaps,
                launcherMultipleImages = launcherMultipleImages
            )
        }
    }
}
