package com.spongycode.spaceegemini.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.navigation.TopBar

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    Scaffold(
        topBar = { TopBar(name = "Settings", navController = navController) }
    ) {
        Column(Modifier.padding(top = it.calculateTopPadding())) {
            SettingsScreenComponent(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreenComponent(viewModel: MainViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val validationState =
        viewModel.validationState.observeAsState().value
    val context = LocalContext.current
    OutlinedTextField(
        singleLine = true,
        value = text,
        onValueChange = { newText -> text = newText },
        placeholder = { Text(text = "Enter your api key") },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp),
        shape = RoundedCornerShape(28),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Black,
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
                if (validationState != MainViewModel.ValidationState.Checking) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        tint = Color.Green,
                        contentDescription = "validate",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                if (text.text.isNotEmpty()) {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    viewModel.validate(context, text.text)
                                }
                            }
                    )
                } else {
                    val strokeWidth = 2.dp
                    CircularProgressIndicator(
                        modifier = Modifier
                            .drawBehind {
                                drawCircle(
                                    Color.Black,
                                    radius = size.width / 2 - strokeWidth.toPx() / 2,
                                    style = Stroke(strokeWidth.toPx())
                                )
                            }
                            .size(30.dp),
                        color = Color.LightGray,
                        strokeWidth = strokeWidth
                    )
                }
            }
        },
        textStyle = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 18.sp
        )
    )
}