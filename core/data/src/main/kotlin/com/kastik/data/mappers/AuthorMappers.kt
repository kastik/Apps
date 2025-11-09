package com.kastik.data.mappers

import com.kastik.database.entities.AnnouncementAuthorEntity
import com.kastik.model.aboard.AnnouncementAuthor
import com.kastik.network.model.aboard.AnnouncementAuthorDto
import com.kastik.network.model.aboard.AnnouncementDto

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