package com.kastik.model.aboard

data class AnnouncementTag(
    val id: Int,
    val title: String,
    val parentId: Int? = null,
    val isPublic: Boolean,
    val mailListName: String? = null
)