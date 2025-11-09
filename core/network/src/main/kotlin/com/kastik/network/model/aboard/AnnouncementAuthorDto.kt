package com.kastik.network.model.aboard

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementAuthorDto(
    val id: Int,
    val name: String
)