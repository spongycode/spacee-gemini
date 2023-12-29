package com.spongycode.spaceegemini.data

import kotlinx.serialization.Serializable

@Serializable
data class DemoApi(
    val success: Boolean,
    val apikey: String? = null,
    val error: String? = null
)
