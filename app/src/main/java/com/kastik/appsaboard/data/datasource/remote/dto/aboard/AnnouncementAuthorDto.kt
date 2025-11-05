package com.kastik.appsaboard.data.datasource.remote.dto.aboard

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementAuthorDto(
    val id: Int,
    val name: String

)