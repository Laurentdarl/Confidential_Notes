package com.laurentdarl.confidentialnotesapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "note")
data class Note(
    val title: String,
    val content: String,
    val dateTime: Date,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
)