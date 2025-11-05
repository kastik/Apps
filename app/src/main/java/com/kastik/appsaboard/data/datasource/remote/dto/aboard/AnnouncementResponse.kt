package com.kastik.appsaboard.data.datasource.remote.dto.aboard

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementResponse(
    val data: List<AnnouncementDto>
)