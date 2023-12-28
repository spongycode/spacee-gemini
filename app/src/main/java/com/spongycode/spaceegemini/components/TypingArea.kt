package com.spongycode.spaceegemini.components

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
    galleryLauncher: ManagedActivityResultLauncher<String, List<@JvmSuppressWildcards Uri>>? = null,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val isGenerating: Boolean? = when (apiType) {
        ApiType.SINGLE_CHAT -> viewModel.singleResponse.observeAsState().value?.lastOrNull()?.isGenerating
        ApiType.IMAGE_CHAT -> viewModel.imageResponse.observeAsState().value?.lastOrNull()?.isGenerating
        ApiType.MULTI_CHAT -> viewModel.conversationList.observeAsState().value?.lastOrNull()?.isGenerating
    }
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                bottom = 10.dp,
                end = 10.dp,
                start = if (apiType == ApiType.SINGLE_CHAT) 10.dp else 0.dp
            )
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {


        var expanded by remember { mutableStateOf(false) }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    permissionLauncher?.launch(Manifest.permission.CAMERA)
                }
            ) {
                Image(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(id = R.drawable.add_camera_icon),
                    contentDescription = "camera"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Camera", fontSize = 15.sp, fontWeight = FontWeight.W600)
            }
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    galleryLauncher?.launch("image/*")
                }
            ) {
                Image(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(id = R.drawable.add_gallery_icon),
                    contentDescription = "gallery"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Gallery", fontSize = 15.sp, fontWeight = FontWeight.W600)
            }
        }


        when (apiType) {
            ApiType.MULTI_CHAT -> IconButton(onClick = { viewModel.clearContext() }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.refresh),
                    tint = Color.Black,
                    contentDescription = "refresh"
                )
            }

            ApiType.IMAGE_CHAT -> IconButton(onClick = {
                expanded = true
            }
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.add_icon),
                    contentDescription = "add"
                )
            }

            ApiType.SINGLE_CHAT -> Unit
        }

        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            placeholder = { Text(text = "Ask a question") },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White),
            shape = RoundedCornerShape(28),
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
            maxLines = 5,
            trailingIcon = {
                Box(
                    modifier = Modifier.padding(end = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isGenerating != true) {
                        Icon(
                            painter = painterResource(id = R.drawable.send_icon),
                            tint = Color.Black,
                            contentDescription = "send",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    if (text.text
                                            .trim()
                                            .isNotEmpty() && (apiType != ApiType.IMAGE_CHAT || bitmaps!!.isNotEmpty())
                                    ) {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                        when (apiType) {
                                            ApiType.SINGLE_CHAT -> viewModel.makeSingleTurnQuery(
                                                context,
                                                text.text.trim()
                                            )

                                            ApiType.MULTI_CHAT -> viewModel.makeMultiTurnQuery(
                                                context,
                                                text.text.trim()
                                            )

                                            ApiType.IMAGE_CHAT -> viewModel.makeImageQuery(
                                                context,
                                                text.text.trim(),
                                                bitmaps!!
                                            )
                                        }
                                        text = TextFieldValue("")
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
}