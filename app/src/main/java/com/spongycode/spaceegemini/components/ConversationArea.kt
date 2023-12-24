package com.spongycode.spaceegemini.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spongycode.spaceegemini.ApiType
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.data.Message

@Composable
fun ConversationArea(
    viewModel: MainViewModel,
    apiType: ApiType
) {
    val response: List<Message>? = when (apiType) {
        ApiType.SINGLE_CHAT -> viewModel.singleResponse.observeAsState().value?.toList()
        ApiType.IMAGE_CHAT -> viewModel.imageResponse.observeAsState().value?.toList()
        ApiType.MULTI_CHAT -> viewModel.conversationList.observeAsState().value?.toList()
    }
    LazyColumn(
        reverseLayout = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        items(response!!.reversed()) { message ->
            MessageItem(text = message.text, mode = message.mode)
        }
    }
}
