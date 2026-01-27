package com.kastik.apps.core.model.aboard

import kotlinx.collections.immutable.ImmutableList

data class Profile(
    val id: Int,
    val name: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val isAuthor: Boolean,
    val isAdmin: Boolean,
    val lastLoginAt: String,
    val uid: String,
    val deletedAt: String?
)

//TODO Move this out of here to a dedicated ui package
data class UserData(
    val profile: Profile,
    val subscriptions: ImmutableList<Tag>
)