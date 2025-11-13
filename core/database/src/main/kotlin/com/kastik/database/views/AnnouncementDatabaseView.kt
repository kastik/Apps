package com.kastik.database.views

import androidx.room.DatabaseView

@DatabaseView(
    """
    SELECT
        a.id AS announcementId,
        a.title AS title,
        au.name AS authorName,
        a.updatedAt AS date,
        a.body AS body,

    COALESCE(GROUP_CONCAT(DISTINCT t.tagId || '|' || t.title),'') AS tags,
    COALESCE(GROUP_CONCAT(DISTINCT att.filename || '|' || att.id),'') AS attachments

    FROM announcements AS a

    LEFT JOIN announcement_authors AS au
        ON a.authorId = au.id

    LEFT JOIN announcement_tag_cross_ref AS x
        ON a.id = x.announcementId

    LEFT JOIN announcement_tags AS t
        ON x.tagId = t.tagId

    LEFT JOIN announcement_attachments AS att
        ON a.id = att.announcementId

    GROUP BY a.id
    """
)
data class AnnouncementDatabaseView(
    val announcementId: Int,
    val title: String,
    val authorName: String,
    val tags: String,
    val attachments: String,
    val date: String,
    val body: String
)