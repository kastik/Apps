package com.kastik.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcement_tags")
data class AnnouncementTagEntity(
    @PrimaryKey val tagId: Int,
    val title: String,
    val tagParentId: Int?,
    val isPublic: Boolean,
    val maillist_name: String?
)

@Entity(
    tableName = "announcement_tag_cross_ref",
    primaryKeys = ["announcementId", "tagId"]
)
data class AnnouncementTagCrossRef(
    val announcementId: Int,
    val tagId: Int
)
