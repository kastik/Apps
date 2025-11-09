package com.kastik.model.aboard

data class AnnouncementAttachment(
    val id: Int,
    val announcementId: Int,
    val filename: String,
    val filesize: Long,
    val mimeType: String,
    val attachmentUrl: String,
    val attachmentUrlView: String
)