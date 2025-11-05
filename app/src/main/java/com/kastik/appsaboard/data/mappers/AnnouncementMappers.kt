package com.kastik.appsaboard.data.mappers

import com.kastik.appsaboard.data.datasource.remote.dto.aboard.AnnouncementAttachmentDto
import com.kastik.appsaboard.data.datasource.remote.dto.aboard.AnnouncementAuthorDto
import com.kastik.appsaboard.data.datasource.remote.dto.aboard.AnnouncementDto
import com.kastik.appsaboard.data.datasource.remote.dto.aboard.AnnouncementTagDto
import com.kastik.appsaboard.domain.models.aboard.Announcement
import com.kastik.appsaboard.domain.models.aboard.AnnouncementAttachment
import com.kastik.appsaboard.domain.models.aboard.AnnouncementAuthor
import com.kastik.appsaboard.domain.models.aboard.AnnouncementTag


fun AnnouncementDto.toAnnouncement(): Announcement =
    Announcement(
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


private fun AnnouncementTagDto.toAnnouncement(): AnnouncementTag =
    AnnouncementTag(
        id = id,
        title = title,
        parentId = parentId,
        isPublic = isPublic,
        mailListName = mailListName
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

private fun AnnouncementAuthorDto.toAnnouncementAuthor(): AnnouncementAuthor =
    AnnouncementAuthor(
        id = id,
        name = name
    )


//Todo after fetching categories is implemented
fun String.toCategoryName(): String = "Category"
