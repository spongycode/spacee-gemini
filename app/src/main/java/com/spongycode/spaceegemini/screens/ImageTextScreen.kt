package com.spongycode.spaceegemini.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.request.ImageRequest
import com.spongycode.spaceegemini.ApiType
import com.spongycode.spaceegemini.components.ConversationArea
import com.spongycode.spaceegemini.components.TypingArea
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.navigation.TopBar
import com.spongycode.util.ImageHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ImageText(viewModel: MainViewModel, navController: NavHostController) {

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
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                ConversationArea(viewModel, apiType = ApiType.IMAGE_CHAT)
            }
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(bitmaps.size) { index ->
                    val bitmap = bitmaps[index]
                    val iconSize = 24.dp
                    val offsetInPx =
                        LocalDensity.current.run { ((iconSize - 5.dp) / 2).roundToPx() }

                    Box(
                        modifier = Modifier
                            .padding(iconSize / 2)
                    ) {
                        Card {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(5.dp))
                            )
                        }
                        IconButton(
                            onClick = { bitmaps.remove(bitmap) },
                            modifier = Modifier
                                .offset {
                                    IntOffset(x = +offsetInPx, y = -offsetInPx)
                                }
                                .clip(CircleShape)
                                .background(Red)
                                .size(iconSize)
                                .align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "close",
                                tint = White
                            )
                        }
                    }
                }
            }
            TypingArea(
                viewModel = viewModel,
                apiType = ApiType.IMAGE_CHAT,
                bitmaps = bitmaps,
                launcherMultipleImages = launcherMultipleImages
            )
        }
    }
}
