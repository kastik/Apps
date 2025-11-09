package com.kastik.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "announcement_attachments",
    foreignKeys = [
        ForeignKey(
            entity = AnnouncementEntity::class,
            parentColumns = ["id"],
            childColumns = ["announcementId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("announcementId")]
)
data class AnnouncementAttachmentEntity(
    @PrimaryKey val id: Int,
    val announcementId: Int,
    val filename: String,
    val filesize: Long,
    val mimetype: String,
    val attachment_url: String,
    val attachment_url_view: String,
)