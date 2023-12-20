package com.spongycode.spaceegemini

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _response = MutableLiveData<String>("Response...")
    val response: LiveData<String> = _response
    private var model: GenerativeModel? = null
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