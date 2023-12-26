package com.spongycode.spaceegemini.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spongycode.spaceegemini.Mode

@Entity
data class Message(
    val text: String,
    val mode: Mode,
    val isGenerating: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)