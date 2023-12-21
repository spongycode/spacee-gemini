package com.spongycode.spaceegemini.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.spongycode.spaceegemini.BuildConfig
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _response = MutableLiveData<String>("Response...")
    val response: LiveData<String> = _response

    private val _conversationList = MutableLiveData(mutableStateListOf<String>())
    val conversationList: MutableLiveData<SnapshotStateList<String>> = _conversationList

    private var model: GenerativeModel? = null
    private var chat: Chat? = null
    fun makeQuery(prompt: String) {
        if (model == null) {
            model = getModel()
        }
        viewModelScope.launch {
            _response.value = "Generating..."
            val answer: GenerateContentResponse = model?.generateContent(prompt)!!
            _response.value = answer.text
        }
    }

    fun makeConversationQuery(prompt: String) {
        _conversationList.value?.add(prompt)
        _conversationList.value?.add("Generating...")

        if (model == null) {
            model = getModel()
        }
        if (chat == null) {
            chat = getChat()
        }
        viewModelScope.launch {
            val singleResponse = chat?.sendMessage(prompt)
            singleResponse?.text?.let {
                _conversationList.value?.removeLastOrNull()
                _conversationList.value?.add(it)
            }
        }
    }

    private fun getChat() = model?.startChat(listOf())

    private fun getModel() = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.API_KEY,
        safetySettings = listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.NONE),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.NONE),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE),
        )
    )
}