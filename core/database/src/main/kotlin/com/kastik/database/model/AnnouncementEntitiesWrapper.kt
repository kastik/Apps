package com.kastik.database.model

import com.kastik.database.entities.AnnouncementAttachmentEntity
import com.kastik.database.entities.AnnouncementAuthorEntity
import com.kastik.database.entities.AnnouncementEntity
import com.kastik.database.entities.AnnouncementTagCrossRef
import com.kastik.database.entities.AnnouncementTagEntity

data class AnnouncementEntityWrapper(
    val announcement: AnnouncementEntity,
    val author: AnnouncementAuthorEntity,
    val tags: List<AnnouncementTagEntity>,
    val tagCrossRefs: List<AnnouncementTagCrossRef>,
    val attachments: List<AnnouncementAttachmentEntity>,
)