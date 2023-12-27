package com.spongycode.spaceegemini.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.navigation.SetApi
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
            Button(
                onClick = {
                    navController.navigate(SetApi.route)
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Change Api Key",
                    fontSize = 15.sp
                )
            }
        }
    }
}