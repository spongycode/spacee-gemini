package com.spongycode.spaceegemini.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spongycode.spaceegemini.navigation.TopBar

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AboutScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopBar(name = "About", navController = navController) }
    ) {
        Column(Modifier.padding(top = 50.dp)) {
            AboutScreenComponent()
        }
    }
}

@Composable
fun AboutScreenComponent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(5.dp),
            shape = RoundedCornerShape(15),
            colors = CardDefaults.cardColors(Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 200.dp, start = 5.dp, end = 5.dp)

        )
        {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "This Application Developed  By",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "Himanshu & Vasanth",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "Version 1.0",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.Gray,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "All rights are reserved",
            fontSize = 20.sp,
            fontWeight = FontWeight.W500,
            color = Color.Gray,
            modifier = Modifier.padding(5.dp),
        )
    }
}