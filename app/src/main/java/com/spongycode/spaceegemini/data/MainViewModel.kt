package com.spongycode.spaceegemini.data

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.spongycode.spaceegemini.Mode
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _singleResponse = MutableLiveData(mutableStateListOf<Message>())
    val singleResponse: MutableLiveData<SnapshotStateList<Message>> = _singleResponse

    private val _conversationList = MutableLiveData(mutableStateListOf<Message>())
    val conversationList: MutableLiveData<SnapshotStateList<Message>> = _conversationList

    private val _imageResponse = MutableLiveData(mutableStateListOf<Message>())
    val imageResponse: MutableLiveData<SnapshotStateList<Message>> = _imageResponse

    private var model: GenerativeModel? = null
    private var visionModel: GenerativeModel? = null
    private var chat: Chat? = null
    fun makeQuery(prompt: String) {
        _singleResponse.value?.clear()
        _singleResponse.value?.add(Message(text = prompt, mode = Mode.USER))
        _singleResponse.value?.add(Message(text = "Generating...", mode = Mode.GEMINI))
        if (model == null) {
            model = getModel()
        }
        viewModelScope.launch {
            val singleAnswer: GenerateContentResponse = model?.generateContent(prompt)!!
            singleAnswer.text?.let {
                _singleResponse.value?.removeLastOrNull()
                _singleResponse.value?.add(Message(text = it.trim(), mode = Mode.GEMINI))
            }
        }
    }

    fun makeConversationQuery(prompt: String) {
        _conversationList.value?.add(Message(text = prompt, mode = Mode.USER))
        _conversationList.value?.add(Message(text = "Generating...", mode = Mode.GEMINI))

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
                _conversationList.value?.add(Message(text = it.trim(), mode = Mode.GEMINI))
            }
        }
    }

    fun makeImageQuery(prompt: String, bitmaps: List<Bitmap>) {
        _imageResponse.value?.clear()
        _imageResponse.value?.add(Message(text = prompt, mode = Mode.USER))
        _imageResponse.value?.add(Message(text = "Generating...", mode = Mode.GEMINI))
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
                _imageResponse.value?.add(Message(text = it.trim(), mode = Mode.GEMINI))
            }
        }
    }

    fun clearContext() {
        _conversationList.value?.clear()
        chat = getChat()
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