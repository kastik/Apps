package com.kastik.database.dao

import AnnouncementPreviewDatabaseView
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kastik.database.entities.AnnouncementAttachmentEntity
import com.kastik.database.entities.AnnouncementAuthorEntity
import com.kastik.database.entities.AnnouncementEntity
import com.kastik.database.entities.AnnouncementTagCrossRef
import com.kastik.database.entities.AnnouncementTagEntity
import com.kastik.database.model.AnnouncementEntityWrapper
import com.kastik.database.views.AnnouncementDatabaseView
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Query("SELECT * FROM announcementpreviewdatabaseview")
    fun getPagingAnnouncementPreviews(): PagingSource<Int, AnnouncementPreviewDatabaseView>

    @Query("SELECT * FROM announcementdatabaseview WHERE announcementId = :id")
    suspend fun getAnnouncementWithId(id: Int): AnnouncementDatabaseView


    @Query("SELECT * FROM announcement_authors")
    fun getAuthors(): Flow<List<AnnouncementAuthorEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAuthor(author: AnnouncementAuthorEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAttachments(attachments: List<AnnouncementAttachmentEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTags(tags: List<AnnouncementTagEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTagCrossRefs(crossRefs: List<AnnouncementTagCrossRef>)


    @Query("DELETE FROM announcements")
    suspend fun clearAllAnnouncements()

    @Query("DELETE FROM announcement_attachments")
    suspend fun clearAttachments()

    @Query("DELETE FROM announcement_tags")
    suspend fun clearTags()

    @Query("DELETE FROM announcement_authors")
    suspend fun clearAuthors()

    @Query("DELETE FROM announcement_tag_cross_ref")
    suspend fun clearTagCrossRefs()


    @Transaction
    suspend fun addAnnouncement(
        announcement: AnnouncementEntityWrapper
    ) {
        insertAnnouncement(announcement.announcement)
        insertAuthor(announcement.author)
        insertAttachments(announcement.attachments)
        insertTags(announcement.tags)
        insertTagCrossRefs(announcement.tagCrossRefs)
    }

    @Transaction
    suspend fun clearDatabase() {
        clearAllAnnouncements()
        clearAttachments()
        clearTagCrossRefs()
    }
}