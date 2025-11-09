package com.kastik.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation


data class AnnouncementWithRelations(
    @Embedded val announcement: AnnouncementEntity,

    @Relation(
        parentColumn = "authorId",
        entityColumn = "id"
    )
    val author: AnnouncementAuthorEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "announcementId"
    )
    val attachments: List<AnnouncementAttachmentEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "tagId",
        associateBy = Junction(
            value = AnnouncementTagCrossRef::class,
            parentColumn = "announcementId",
            entityColumn = "tagId"
        )
    )
    val tags: List<AnnouncementTagEntity>
)


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
