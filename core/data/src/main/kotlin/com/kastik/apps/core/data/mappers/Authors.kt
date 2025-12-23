package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.network.model.aboard.AnnouncementAuthorDto
import com.kastik.apps.core.network.model.aboard.AuthorDto

internal fun AuthorDto.toAuthor() = Author(
    id = id,
    name = name,
    announcementCount = announcementCount
)

internal fun AnnouncementAuthorDto.toAuthorEntity() = AuthorEntity(
    id = id,
    name = name,
)

internal fun AuthorDto.toAuthorEntity() = AuthorEntity(
    id = id,
    name = name,
    announcementCount = announcementCount
)

internal fun AuthorEntity.toAuthor() = Author(
    id = id,
    name = name,
    announcementCount = announcementCount ?: 0,
)