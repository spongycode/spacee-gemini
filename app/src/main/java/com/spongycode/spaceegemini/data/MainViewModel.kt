package com.spongycode.spaceegemini.data

import android.graphics.Bitmap
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
import com.google.ai.client.generativeai.type.content
import com.spongycode.spaceegemini.BuildConfig
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _response = MutableLiveData<String>("Response...")
    val response: LiveData<String> = _response

    private val _conversationList = MutableLiveData(mutableStateListOf<String>())
    val conversationList: MutableLiveData<SnapshotStateList<String>> = _conversationList

    private val _imageResponse = MutableLiveData(mutableStateListOf<String>())
    val imageResponse: MutableLiveData<SnapshotStateList<String>> = _imageResponse

    private var model: GenerativeModel? = null
    private var visionModel: GenerativeModel? = null
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

    fun makeImageQuery(prompt: String, bitmaps: List<Bitmap>) {
        _imageResponse.value?.clear()
        _imageResponse.value?.add(prompt)
        _imageResponse.value?.add("Generating...")
        if (visionModel == null) {
            visionModel = getModel(true)
        }
        val inputContent = content {
            bitmaps.forEach {
                image(it)
            }
            text(prompt)
        }
        viewModelScope.launch {

            val imageAnswer = visionModel!!.generateContent(inputContent)
            imageAnswer.text?.let {
                _imageResponse.value?.removeLastOrNull()
                _imageResponse.value?.add(it)
            }
        }
    }

    private fun getChat() = model?.startChat(listOf())

    private fun getModel(vision: Boolean = false) = GenerativeModel(
        modelName = if (vision) "gemini-pro-vision" else "gemini-pro",
        apiKey = BuildConfig.API_KEY,
        safetySettings = listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.NONE),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.NONE),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE),
        )
    )
}