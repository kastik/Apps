package com.kastik.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcement_authors")
data class AnnouncementAuthorEntity(
    @PrimaryKey val id: Int,
    val name: String,
)
