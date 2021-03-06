package com.laurentdarl.confidentialnotesapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "note")
data class Note(
    val title: String,
    val content: String,
    val date_time: Date?,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
): Serializable