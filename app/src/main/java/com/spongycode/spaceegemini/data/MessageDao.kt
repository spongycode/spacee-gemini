package com.spongycode.spaceegemini.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MessageDao {
    @Upsert
    suspend fun upsertMessage(message: Message)

    @Query("SELECT * FROM message")
    fun getAllMessage(): LiveData<List<Message>>

    @Query("DELETE FROM message")
    suspend fun deleteAllMessages()
}