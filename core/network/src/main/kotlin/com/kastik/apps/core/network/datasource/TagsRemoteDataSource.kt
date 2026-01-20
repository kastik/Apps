package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.di.AnnRetrofit
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.SubscribableTagsDto
import com.kastik.apps.core.network.model.aboard.TagsResponseDto

interface TagsRemoteDataSource {
    suspend fun fetchAnnouncementTags(): TagsResponseDto
    suspend fun fetchSubscribableTags(): List<SubscribableTagsDto>
}

internal class TagsRemoteDataSourceImpl(
    @AnnRetrofit private val aboardApiClient: AboardApiClient
) : TagsRemoteDataSource {
    override suspend fun fetchAnnouncementTags(): TagsResponseDto =
        aboardApiClient.getTags()

    override suspend fun fetchSubscribableTags(): List<SubscribableTagsDto> {
        return aboardApiClient.getUserSubscribableTags()
    }

}