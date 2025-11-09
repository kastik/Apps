package com.kastik.model.aboard

data class Announcement(
    val id: Int,


    val title: String,
    val engTitle: String? = null,
    val body: String,
    val engBody: String? = null,
    val preview: String? = null,
    val engPreview: String? = null,
    val hasEng: Boolean? = null,


    val createdAt: String,
    val updatedAt: String,

    val isPinned: Boolean,
    val pinnedUntil: String? = null,

    val isEvent: Boolean? = null,
    val eventStartTime: String? = null,
    val eventEndTime: String? = null,
    val eventLocation: String? = null,
    val gmaps: String? = null,

    val tags: List<AnnouncementTag> = emptyList(),
    val attachments: List<AnnouncementAttachment> = emptyList(),
    val author: AnnouncementAuthor,

    val announcementUrl: String
)