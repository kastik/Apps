package com.kastik.data.mappers

import AnnouncementPreviewDatabaseView
import android.util.Log
import com.kastik.database.entities.AnnouncementAttachmentEntity
import com.kastik.database.entities.AnnouncementAuthorEntity
import com.kastik.database.entities.AnnouncementEntity
import com.kastik.database.entities.AnnouncementTagCrossRef
import com.kastik.database.entities.AnnouncementTagEntity
import com.kastik.database.model.AnnouncementEntityWrapper
import com.kastik.database.views.AnnouncementDatabaseView
import com.kastik.model.aboard.AnnouncementPreview
import com.kastik.model.aboard.AnnouncementView
import com.kastik.model.aboard.AnnouncementViewAttachment
import com.kastik.model.aboard.AnnouncementViewTag
import com.kastik.network.model.aboard.AnnouncementDto


fun AnnouncementDto.toRoomEntities(): AnnouncementEntityWrapper {
    return AnnouncementEntityWrapper(
        announcement = toEntity(),
        author = toAuthorEntity(),
        tags = toTagEntities(),
        tagCrossRefs = toTagCrossRefs(),
        attachments = toAttachmentEntities()

    )
}

private fun AnnouncementDto.toEntity() = AnnouncementEntity(
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

private fun AnnouncementDto.toAuthorEntity() = AnnouncementAuthorEntity(
    id = author.id,
    name = author.name
)

private fun AnnouncementDto.toAttachmentEntities() = attachments.map { att ->
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

private fun AnnouncementDto.toTagEntities() = tags.map { tag ->
    AnnouncementTagEntity(
        tagId = tag.id,
        title = tag.title,
        tagParentId = tag.parentId,
        isPublic = tag.isPublic,
        maillist_name = tag.mailListName,
    )
}

private fun AnnouncementDto.toTagCrossRefs() = tags.map { tag ->
    AnnouncementTagCrossRef(
        announcementId = this.id,
        tagId = tag.id
    )
}


internal fun AnnouncementPreviewDatabaseView.toDomain() = AnnouncementPreview(
    id = announcementId,
    title = title,
    preview = preview,
    tags = tags,
    attachments = attachments,
    author = authorName,
    date = date,
)

internal fun AnnouncementDatabaseView.toDomain() = AnnouncementView(
    id = this.announcementId,
    title = title,
    body = body,
    tags = this.decodedTags(),
    attachments = this.decodedAttachments(),
    author = authorName,
    date = date
)

private fun AnnouncementDatabaseView.decodedTags(): List<AnnouncementViewTag> {
    return if (tags.isBlank()) emptyList()
    else tags.split(",").map {
        val (id, title) = it.split("|")
        AnnouncementViewTag(id.toInt(), title)
    }
}

private fun AnnouncementDatabaseView.decodedAttachments(): List<AnnouncementViewAttachment> {
    return if (attachments.isBlank()) emptyList()
    else attachments.split(",").map {
        Log.d("MyLog", "Mapping attachment: $it")
        val (filename, id) = it.split("|")
        AnnouncementViewAttachment(filename, id.toInt())
    }
}