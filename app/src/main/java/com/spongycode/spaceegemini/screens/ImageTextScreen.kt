package com.spongycode.spaceegemini.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.request.ImageRequest
import com.spongycode.spaceegemini.R
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.util.ImageHelper
import kotlinx.coroutines.launch

@Composable
fun ImageText(viewModel: MainViewModel) {

    val bitmaps: SnapshotStateList<Bitmap> = remember {
        mutableStateListOf()
    }
    val context = LocalContext.current

    val imageRequestBuilder = ImageRequest.Builder(context)
    val imageLoader = ImageLoader.Builder(context).build()

    val coroutineScope = rememberCoroutineScope()

    val launcherMultipleImages = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
    ) {
        it.forEach { uri ->
            coroutineScope.launch {
                ImageHelper.scaleDownBitmap(uri, imageRequestBuilder, imageLoader)?.let { bitmap ->
                    bitmaps.add(bitmap)
                }
            }
        }

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(1f)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            ConversationAreaWithImage(viewModel)
        }
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(bitmaps.size) { index ->
                val bitmap = bitmaps[index]
                val iconSize = 24.dp
                val offsetInPx = LocalDensity.current.run { ((iconSize - 5.dp) / 2).roundToPx() }

                Box(
                    modifier = Modifier
                        .padding(iconSize / 2)
                ) {
                    Card {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(5.dp))
                        )
                    }
                    IconButton(
                        onClick = { bitmaps.remove(bitmap) },
                        modifier = Modifier
                            .offset {
                                IntOffset(x = +offsetInPx, y = -offsetInPx)
                            }
                            .clip(CircleShape)
                            .background(Red)
                            .size(iconSize)
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "close",
                            tint = White
                        )
                    }
                }
            }
        }
        TypingAreaWithImage(viewModel, bitmaps, launcherMultipleImages)
    }
}

@Composable
fun ConversationAreaWithImage(
    viewModel: MainViewModel
) {
    val imageResponse = viewModel.imageResponse.observeAsState().value?.toList()
    LazyColumn(
        reverseLayout = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        items(imageResponse!!.reversed()) { ele ->
            Text(
                text = ele,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TypingAreaWithImage(
    viewModel: MainViewModel,
    bitmaps: SnapshotStateList<Bitmap>,
    launcherMultipleImages: ManagedActivityResultLauncher<String, List<@JvmSuppressWildcards Uri>>
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var text by remember { mutableStateOf(TextFieldValue("")) }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = {
                launcherMultipleImages.launch("image/*")
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                contentDescription = "add"
            )
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
                                viewModel.makeImageQuery(text.text, bitmaps)
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