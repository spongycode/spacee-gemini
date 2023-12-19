package com.spongycode.spaceegemini

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.spongycode.spaceegemini.ui.theme.SpaceeGeminiTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpaceeGeminiTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Row {
                        var text by remember { mutableStateOf(TextFieldValue("")) }
                        LabelAndPlaceHolder(text, onValueChange = { newText -> text = newText })
                        TextButton(onClick = { viewModel.makeQuery(text.text) }) {
                            Text(text = "Search")
                        }
                    }
                    Text(text = viewModel.response.observeAsState().value.toString())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelAndPlaceHolder(
    text: TextFieldValue, onValueChange: (TextFieldValue) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        label = { Text(text = "Prompt") },
        placeholder = { Text(text = "Ask anything...") },
    )
}