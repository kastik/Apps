package com.kastik.model.aboard

data class AnnouncementPreview(
    val id: Int,
    val title: String,
    val preview: String?,
    val author: String,
    val tags: List<String> = emptyList(),
    val attachments: List<String> = emptyList(),
    val date: String
)

data class AnnouncementView(
    val id: Int,
    val title: String,
    val author: String,
    val tags: List<AnnouncementViewTag> = emptyList(),
    val attachments: List<AnnouncementViewAttachment> = emptyList(),
    val date: String,
    val body: String
)

data class AnnouncementViewTag(
    val id: Int,
    val title: String
)

data class AnnouncementViewAttachment(
    val filename: String,
    val id: Int
)