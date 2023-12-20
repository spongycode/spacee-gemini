package com.spongycode.spaceegemini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.spongycode.spaceegemini.navigation.MyNavigation
import com.spongycode.spaceegemini.ui.theme.SpaceeGeminiTheme

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpaceeGeminiTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {

                    MyNavigation(
                        viewModel = viewModel,
                        onSearchClicked = { query -> viewModel.makeQuery(query) })
                }
            }
        }
    }
}