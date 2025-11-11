package com.kastik.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

@Entity(
    tableName = "announcement_tag_cross_ref",
    primaryKeys = ["announcementId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = AnnouncementEntity::class,
            parentColumns = ["id"],
            childColumns = ["announcementId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = AnnouncementTagEntity::class,
            parentColumns = ["tagId"],
            childColumns = ["tagId"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index("announcementId"),
        Index("tagId")
    ]
)
data class AnnouncementTagCrossRef(
    val announcementId: Int,
    val tagId: Int
)