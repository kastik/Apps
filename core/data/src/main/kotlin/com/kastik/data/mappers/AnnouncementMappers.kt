package com.kastik.data.mappers

import AnnouncementPreviewDatabaseView
import com.kastik.database.entities.AnnouncementAttachmentEntity
import com.kastik.database.entities.AnnouncementEntity
import com.kastik.database.entities.AnnouncementTagCrossRef
import com.kastik.database.entities.AnnouncementTagEntity
import com.kastik.database.views.AnnouncementDatabaseView
import com.kastik.model.aboard.AnnouncementPreview
import com.kastik.model.aboard.AnnouncementView
import com.kastik.network.model.aboard.AnnouncementDto


fun AnnouncementDto.toEntity() = AnnouncementEntity(
    id = id,
    title = title,
    engTitle = engTitle,
    body = body,
    engBody = engBody,
    hasEng = hasEng ?: false,
    preview = preview,
    engPreview = engPreview,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isPinned = isPinned,
    pinnedUntil = pinnedUntil,
    isEvent = isEvent,
    eventStartTime = eventStartTime,
    eventEndTime = eventEndTime,
    eventLocation = eventLocation,
    gmaps = gmaps,
    announcementUrl = announcementUrl,
    authorId = author.id,
)


fun AnnouncementPreviewDatabaseView.toDomain() = AnnouncementPreview(
    id = announcementId,
    title = title,
    preview = preview,
    tags = tags,
    attachments = attachments,
    author = authorName,
    date = date,
)

fun AnnouncementDatabaseView.toDomain() = AnnouncementView(
    id = this.announcementId,
    title = title,
    body = body,
    tags = tags,
    attachments = attachments,
    author = authorName,
    date = date
)

fun AnnouncementDto.toAttachmentEntities() = attachments.map { att ->
    AnnouncementAttachmentEntity(
        id = att.id,
        announcementId = this.id,
        filename = att.filename,
        filesize = att.filesize,
        mimetype = att.mimeType,
        attachment_url = att.attachmentUrl,
        attachment_url_view = att.attachmentUrlView
    )
}

fun AnnouncementDto.toTagEntities() = tags.map { tag ->
    AnnouncementTagEntity(
        tagId = tag.id,
        title = tag.title,
        tagParentId = tag.parentId,
        isPublic = tag.isPublic,
        maillist_name = tag.mailListName,
    )
}

fun AnnouncementDto.toTagCrossRefs() = tags.map { tag ->
    AnnouncementTagCrossRef(
        announcementId = this.id,
        tagId = tag.id
    )
}