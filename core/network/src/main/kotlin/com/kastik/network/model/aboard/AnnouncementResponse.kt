package com.kastik.network.model.aboard

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementResponse(
    val data: List<AnnouncementDto>,
    val meta: AnnouncementMeta
)