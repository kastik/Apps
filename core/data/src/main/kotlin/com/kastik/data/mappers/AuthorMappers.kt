package com.kastik.data.mappers

import com.kastik.database.entities.AnnouncementAuthorEntity
import com.kastik.model.aboard.AnnouncementAuthor
import com.kastik.model.aboard.Pivot
import com.kastik.model.aboard.UserProfile
import com.kastik.model.aboard.UserSubscribedTag
import com.kastik.network.model.aboard.AnnouncementAuthorDto
import com.kastik.network.model.aboard.AnnouncementDto
import com.kastik.network.model.aboard.PivotDto
import com.kastik.network.model.aboard.UserProfileDto
import com.kastik.network.model.aboard.UserSubscribedTagDto

fun AnnouncementAuthorDto.toAnnouncementAuthor(): AnnouncementAuthor = AnnouncementAuthor(
    id = id, name = name
)

fun AnnouncementAuthorEntity.toDomain(): AnnouncementAuthor =
    AnnouncementAuthor(
        id = id, name = name
    )

fun AnnouncementDto.toAuthorEntity() = AnnouncementAuthorEntity(
    id = author.id,
    name = author.name
)

fun PivotDto.toDomain() = Pivot(
    userId = userId,
    tagId = userId
)


fun UserSubscribedTagDto.toDomain() = UserSubscribedTag(
    id = id,
    title = title,
    parentId = parentId,
    isPublic = isPublic,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt,
    mailListName = mailListName,
    pivot = pivot.toDomain()
)

fun UserProfileDto.toDomain() = UserProfile(
    id = id,
    name = name,
    nameEng = nameEng,
    email = email,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isAuthor = isAuthor,
    isAdmin = isAdmin,
    lastLoginAt = lastLoginAt,
    uid = uid,
    deletedAt = deletedAt
)