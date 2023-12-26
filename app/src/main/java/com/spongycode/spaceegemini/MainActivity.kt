package com.spongycode.spaceegemini

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.data.MessageDatabase
import com.spongycode.spaceegemini.navigation.MyNavigation
import com.spongycode.spaceegemini.screens.SetApiScreen
import com.spongycode.spaceegemini.ui.theme.SpaceeGeminiTheme
import com.spongycode.util.datastore
import com.spongycode.util.getApiKey
import kotlinx.coroutines.runBlocking

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(applicationContext, MessageDatabase::class.java, "message.db").build()
    }

    private val viewModel by viewModels<MainViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(db.dao) as T
                }
            }
        }
    )

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpaceeGeminiTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (runBlocking { applicationContext.datastore.getApiKey().isEmpty() }) {
                        SetApiScreen(viewModel) {
                            setContent {
                                MyNavigation(
                                    viewModel = viewModel
                                )
                            }
                        }
                    } else {
                        MyNavigation(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

enum class ApiType {
    SINGLE_CHAT,
    MULTI_CHAT,
    IMAGE_CHAT
}

enum class Mode {
    USER,
    GEMINI
}