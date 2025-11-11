import androidx.room.DatabaseView

@DatabaseView(
    """
    SELECT
        a.id AS announcementId,
        a.title AS title,
        a.preview AS preview,
        au.name AS authorName,
        a.updatedAt AS date,

        COALESCE(GROUP_CONCAT(DISTINCT t.title), '') AS tags,
        COALESCE(GROUP_CONCAT(DISTINCT att.filename), '') AS attachments

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
    ORDER BY a.updatedAt DESC
    """
)
data class AnnouncementPreviewDatabaseView(
    val announcementId: Int,
    val title: String,
    val preview: String,
    val authorName: String,
    val tags: List<String>,
    val attachments: List<String>,
    val date: String
)
