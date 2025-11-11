package com.kastik.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey val id: Int,

    val title: String,
    val engTitle: String?,

    val body: String,
    val engBody: String?,

    val preview: String,
    val engPreview: String?,

    val hasEng: Boolean,

    val createdAt: String,
    val updatedAt: String,

    val isPinned: Boolean,
    val pinnedUntil: String?,

    val isEvent: Boolean?,
    val eventStartTime: String?,
    val eventEndTime: String?,
    val eventLocation: String?,
    val gmaps: String?,

    val authorId: Int,

    val announcementUrl: String
)