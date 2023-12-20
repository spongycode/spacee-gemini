package com.spongycode.spaceegemini.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.navigation.HomeTopBar
import com.spongycode.spaceegemini.navigation.Items
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
               viewModel: MainViewModel,
               onSearchClicked: (String) -> Unit,
               navController:NavHostController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    val currentRoute = navController.currentBackStackEntry?.destination?.route ?: ""
    selectedItemIndex = Items.indexOfFirst { it.title == currentRoute }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(40.dp))
                Items.forEachIndexed { index, item ->
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
                            navController.navigate(item.title)
                            selectedItemIndex = index
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
            Column(modifier = Modifier.padding(top = 60.dp)) {
                HomeScreenComponent(viewModel, onSearchClicked)
            }
        }
    }
}
@ExperimentalComposeUiApi
@Composable
fun HomeScreenComponent(
               viewModel: MainViewModel,
               onSearchClicked: (String) -> Unit
) {
    val response = viewModel.response.observeAsState().value.toString()
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(1f)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(5.dp)
        ){
            item {
                Text(
                    text = response,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                singleLine = true,
                value = text,
                onValueChange = {newText -> text = newText },
                placeholder = { Text(text = "Ask a question") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp),
                shape = RoundedCornerShape(28),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color.Black
                ),
                trailingIcon = {
                    Box(
                        modifier = Modifier.padding(end = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "send",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { onSearchClicked(text.text) })
                    }
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp
                )
            )
        }
    }
}