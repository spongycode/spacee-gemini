package com.spongycode.spaceegemini.data

import android.content.Context
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
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.spongycode.spaceegemini.Mode
import com.spongycode.spaceegemini.R
import com.spongycode.util.datastore
import com.spongycode.util.getApiKey
import com.spongycode.util.storeApiKey
import kotlinx.coroutines.launch

class MainViewModel(private val dao: MessageDao) : ViewModel() {
    private val _singleResponse = MutableLiveData(mutableStateListOf<Message>())
    val singleResponse: LiveData<SnapshotStateList<Message>> = _singleResponse

    private val _conversationList = MutableLiveData(mutableStateListOf<Message>())
    val conversationList: LiveData<SnapshotStateList<Message>> = _conversationList

    private val _imageResponse = MutableLiveData(mutableStateListOf<Message>())
    val imageResponse: LiveData<SnapshotStateList<Message>> = _imageResponse

    private val _validationState = MutableLiveData<ValidationState>(ValidationState.Idle)
    val validationState: LiveData<ValidationState> = _validationState

    private val _isHomeVisit = MutableLiveData<Boolean>(false)
    val isHomeVisit: LiveData<Boolean> = _isHomeVisit

    private var model: GenerativeModel? = null
    private var visionModel: GenerativeModel? = null
    private var chat: Chat? = null

    init {
        dao.getAllMessage().observeForever { allMessages ->
            if (allMessages != null) {
                val snapshotStateList = convertToSnapshotStateList(allMessages)
                _conversationList.postValue(snapshotStateList)
            }
        }
    }

    fun makeQuery(context: Context, prompt: String) {
        _singleResponse.value?.clear()
        _singleResponse.value?.add(Message(text = prompt, mode = Mode.USER))
        _singleResponse.value?.add(
            Message(
                text = context.getString(R.string.generating),
                mode = Mode.GEMINI,
                isGenerating = true
            )
        )
        if (model == null) {
            viewModelScope.launch {
                model = getModel(key = context.datastore.getApiKey())
            }
        }
        viewModelScope.launch {
            var output = ""
            model?.generateContentStream(prompt)?.collect { chunk ->
                output += chunk.text.toString()
                output.trimStart()
                _singleResponse.value?.set(
                    _singleResponse.value!!.lastIndex,
                    Message(text = output, mode = Mode.GEMINI, isGenerating = true)
                )
            }
            _singleResponse.value?.set(
                _singleResponse.value!!.lastIndex,
                Message(text = output, mode = Mode.GEMINI, isGenerating = false)
            )
        }
    }

    fun makeConversationQuery(context: Context, prompt: String) {
        _conversationList.value?.add(Message(text = prompt, mode = Mode.USER))
        _conversationList.value?.add(
            Message(
                text = context.getString(R.string.generating),
                mode = Mode.GEMINI,
                isGenerating = true
            )
        )

        if (model == null) {
            viewModelScope.launch {
                model = getModel(key = context.datastore.getApiKey())
            }
        }
        if (chat == null) {
            chat = getChat()
        }
        viewModelScope.launch {
            var output = ""
            chat?.sendMessageStream(prompt)?.collect { chunk ->
                output += chunk.text.toString()
                output.trimStart()
                _conversationList.value?.set(
                    _conversationList.value!!.lastIndex,
                    Message(text = output, mode = Mode.GEMINI, isGenerating = true)
                )
            }
            _conversationList.value?.set(
                _conversationList.value!!.lastIndex,
                Message(text = output, mode = Mode.GEMINI, isGenerating = false)
            )
            viewModelScope.launch {
                dao.upsertMessage(Message(text = prompt, mode = Mode.USER, isGenerating = false))
                dao.upsertMessage(Message(text = output, mode = Mode.GEMINI, isGenerating = false))
            }
        }
    }

    fun makeImageQuery(context: Context, prompt: String, bitmaps: List<Bitmap>) {
        _imageResponse.value?.clear()
        _imageResponse.value?.add(Message(text = prompt, mode = Mode.USER))
        _imageResponse.value?.add(
            Message(
                text = context.getString(R.string.generating),
                mode = Mode.GEMINI,
                isGenerating = true
            )
        )
        if (visionModel == null) {
            viewModelScope.launch {
                visionModel = getModel(key = context.datastore.getApiKey(), vision = true)
            }
        }
        val inputContent = content {
            bitmaps.take(4).forEach {
                image(it)
            }
            text(prompt)
        }
        viewModelScope.launch {
            var output = ""
            try {
                visionModel?.generateContentStream(inputContent)?.collect { chunk ->
                    output += chunk.text.toString()
                    output.trimStart()
                    _imageResponse.value?.set(
                        _imageResponse.value!!.lastIndex,
                        Message(text = output, mode = Mode.GEMINI, isGenerating = true)
                    )
                }
                _imageResponse.value?.set(
                    _imageResponse.value!!.lastIndex,
                    Message(text = output, mode = Mode.GEMINI, isGenerating = false)
                )
            } catch (_: Exception) {
                _imageResponse.value?.set(
                    _imageResponse.value!!.lastIndex,
                    Message(
                        text = context.getString(R.string.error_occurred),
                        mode = Mode.GEMINI,
                        isGenerating = false
                    )
                )
            }
        }
    }

    fun clearContext() {
        _conversationList.value?.clear()
        chat = getChat()
        viewModelScope.launch {
            dao.deleteAllMessages()
        }
    }

    fun validate(context: Context, apiKey: String) {
        viewModelScope.launch {
            _validationState.value = ValidationState.Checking
            try {
                model = getModel(key = apiKey, vision = false)
                val res = model?.generateContent("Hi")
                if (res?.text?.isNotEmpty() == true) {
                    _validationState.value = ValidationState.Valid
                    context.datastore.storeApiKey(apiKey)
                } else _validationState.value = ValidationState.Invalid
            } catch (e: Exception) {
                _validationState.value = ValidationState.Invalid
            }
        }
    }

    fun resetValidationState() {
        _validationState.value = ValidationState.Idle
    }

    fun makeHomeVisit() {
        _isHomeVisit.value = true
        resetValidationState()
    }

    private fun getChat() = model?.startChat(generatePreviousChats())

    private fun getModel(key: String, vision: Boolean = false) =
        GenerativeModel(
            modelName = if (vision) "gemini-pro-vision" else "gemini-pro",
            apiKey = key,
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.NONE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.NONE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE),
            )
        )

    private fun generatePreviousChats(): List<Content> {
        val history = mutableListOf<Content>()
        for (message in conversationList.value.orEmpty()) {
            history.add(content(role = if (message.mode == Mode.USER) "user" else "model") {
                text(
                    message.text
                )
            })
        }
        return history
    }

    private fun convertToSnapshotStateList(messages: List<Message>): SnapshotStateList<Message> {
        return mutableStateListOf(*messages.toTypedArray())
    }

    sealed class ValidationState {
        object Idle : ValidationState()
        object Checking : ValidationState()
        object Valid : ValidationState()
        object Invalid : ValidationState()
    }
}