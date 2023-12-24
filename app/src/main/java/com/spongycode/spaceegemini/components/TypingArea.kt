package com.spongycode.spaceegemini.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.spaceegemini.ApiType
import com.spongycode.spaceegemini.R
import com.spongycode.spaceegemini.data.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TypingArea(
    viewModel: MainViewModel,
    apiType: ApiType,
    bitmaps: SnapshotStateList<Bitmap>? = null,
    launcherMultipleImages: ManagedActivityResultLauncher<String, List<@JvmSuppressWildcards Uri>>? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (apiType == ApiType.IMAGE_CHAT) {
            IconButton(onClick = { launcherMultipleImages?.launch("image/*") }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                    contentDescription = "add"
                )
            }
        }

        OutlinedTextField(
            singleLine = true,
            value = text,
            onValueChange = { newText -> text = newText },
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
                            .clickable {
                                keyboardController?.hide()
                                when (apiType) {
                                    ApiType.SINGLE_CHAT -> viewModel.makeQuery(text.text)
                                    ApiType.MULTI_CHAT -> viewModel.makeConversationQuery(text.text)
                                    ApiType.IMAGE_CHAT -> viewModel.makeImageQuery(
                                        text.text,
                                        bitmaps!!
                                    )
                                }
                                text = TextFieldValue("")
                            }
                    )
                }
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 18.sp
            )
        )
    }

}