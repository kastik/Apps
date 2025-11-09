package com.kastik.data.mappers

import com.kastik.database.entities.AnnouncementAttachmentEntity
import com.kastik.database.entities.AnnouncementEntity
import com.kastik.database.entities.AnnouncementTagCrossRef
import com.kastik.database.entities.AnnouncementTagEntity
import com.kastik.database.entities.AnnouncementWithRelations
import com.kastik.model.aboard.Announcement
import com.kastik.model.aboard.AnnouncementAttachment
import com.kastik.model.aboard.AnnouncementTag
import com.kastik.network.model.aboard.AnnouncementAttachmentDto
import com.kastik.network.model.aboard.AnnouncementDto
import com.kastik.network.model.aboard.AnnouncementTagDto


fun AnnouncementDto.toAnnouncement(): Announcement = Announcement(
    id = id,
    title = title,
    engTitle = engTitle,
    body = body,
    engBody = engBody,
    preview = preview,
    engPreview = engPreview,
    hasEng = hasEng,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isPinned = isPinned,
    pinnedUntil = pinnedUntil,
    isEvent = isEvent,
    eventStartTime = eventStartTime,
    eventEndTime = eventEndTime,
    eventLocation = eventLocation,
    gmaps = gmaps,
    tags = tags.map { it.toAnnouncement() },
    attachments = attachments.map { it.toAttachment() },
    author = author.toAnnouncementAuthor(),
    announcementUrl = announcementUrl
)


fun List<AnnouncementTag>.toTitleList(): List<String> = map { it.title }


private fun AnnouncementTagDto.toAnnouncement(): AnnouncementTag = AnnouncementTag(
    id = id, title = title, parentId = parentId, isPublic = isPublic, mailListName = mailListName
)

private fun AnnouncementAttachmentDto.toAttachment(): AnnouncementAttachment =
    AnnouncementAttachment(
        id = id,
        announcementId = announcementId,
        filename = filename,
        filesize = filesize,
        mimeType = mimeType,
        attachmentUrl = attachmentUrl,
        attachmentUrlView = attachmentUrlView
    )


private fun AnnouncementTagEntity.toDomain(): AnnouncementTag = AnnouncementTag(
    id = tagId, title = title, parentId = tagParentId, isPublic = isPublic
)

private fun List<AnnouncementTagEntity>.toDomain() = map { it.toDomain() }


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


fun AnnouncementWithRelations.toDomain() = Announcement(
    id = announcement.id,
    title = announcement.title,
    engTitle = announcement.engTitle,
    body = announcement.body,
    engBody = announcement.engBody,
    preview = announcement.preview,
    engPreview = announcement.engPreview,
    hasEng = announcement.engPreview != null,

    createdAt = announcement.createdAt,
    updatedAt = announcement.updatedAt,

    isPinned = announcement.isPinned,
    pinnedUntil = announcement.pinnedUntil,

    isEvent = announcement.isEvent,
    eventStartTime = announcement.eventStartTime,
    eventEndTime = announcement.eventEndTime,
    eventLocation = announcement.eventLocation,
    gmaps = announcement.gmaps,

    tags = tags.toDomain(),
    attachments = emptyList(), // fetched on detail
    author = author.toDomain(),
    announcementUrl = announcement.announcementUrl
)


fun AnnouncementDto.toAttachmentEntities() = attachments.map { att ->
    AnnouncementAttachmentEntity(
        id = att.id,
        announcementId = att.id,
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
        maillist_name = tag.mailListName
    )
}

fun AnnouncementDto.toTagCrossRefs() = tags.map { tag ->
    AnnouncementTagCrossRef(
        announcementId = this.id,
        tagId = tag.id
    )
}