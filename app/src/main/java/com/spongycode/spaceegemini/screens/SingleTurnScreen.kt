package com.spongycode.spaceegemini.screens

import android.annotation.SuppressLint
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spongycode.spaceegemini.ApiType
import com.spongycode.spaceegemini.R
import com.spongycode.spaceegemini.components.ConversationArea
import com.spongycode.spaceegemini.components.TypingArea
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.navigation.HomeTopBar
import com.spongycode.spaceegemini.navigation.TopBar
import com.spongycode.spaceegemini.navigation.items
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SingleTurnScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    val currentRoute = navController.currentBackStackEntry?.destination?.route ?: ""
    viewModel.makeHomeVisit()

    selectedItemIndex = items.indexOfFirst { it.title == currentRoute }


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
}

