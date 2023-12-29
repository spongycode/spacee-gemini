package com.spongycode.spaceegemini.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.request.ImageRequest
import com.spongycode.spaceegemini.ApiType
import com.spongycode.spaceegemini.components.ConversationArea
import com.spongycode.spaceegemini.components.SelectedImageArea
import com.spongycode.spaceegemini.components.TypingArea
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.navigation.HomeTopBar
import com.spongycode.spaceegemini.navigation.items
import com.spongycode.util.ImageHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ImageChatScreen(viewModel: MainViewModel, navController: NavHostController) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    val currentRoute = navController.currentBackStackEntry?.destination?.route ?: ""
    viewModel.makeHomeVisit()

    selectedItemIndex = items.indexOfFirst { it.title == currentRoute }

    val bitmaps: SnapshotStateList<Bitmap> = remember {
        mutableStateListOf()
    }
    val context = LocalContext.current

    val imageRequestBuilder = ImageRequest.Builder(context)
    val imageLoader = ImageLoader.Builder(context).build()

    val coroutineScope = rememberCoroutineScope()

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        if (it != null) {
            bitmaps.add(it)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
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
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(40.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W600,
                                modifier = Modifier.padding(5.dp)
                            )
                        },
                        selected = selectedItemIndex == index,
                        onClick = {
                            if (index != selectedItemIndex) {
                                navController.navigate(item.title)
                                selectedItemIndex = index
                            }
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(5.dp)
                            ) {
                                Image(
                                    painterResource(
                                        id = if (index == selectedItemIndex) {
                                            item.selectedIcon
                                        } else {
                                            item.unselectedIcon
                                        }
                                    ),
                                    contentDescription = item.title
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .size(60.dp)
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = { HomeTopBar(scope, drawerState) }
        ) {
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
                    galleryLauncher = galleryLauncher,
                    permissionLauncher = permissionLauncher
                )
            }
        }
    }
}
