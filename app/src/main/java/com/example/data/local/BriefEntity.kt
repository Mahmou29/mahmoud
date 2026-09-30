package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brief_history")
data class BriefEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val rawText: String,
    val targetTeam: String,
    val thinkingMode: String,
    val resultJson: String,
    val brandOrClientTag: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
