package com.kastik.database.db

import AnnouncementPreviewDatabaseView
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kastik.database.converters.StringListConverter
import com.kastik.database.dao.AnnouncementDao
import com.kastik.database.entities.AnnouncementAttachmentEntity
import com.kastik.database.entities.AnnouncementAuthorEntity
import com.kastik.database.entities.AnnouncementEntity
import com.kastik.database.entities.AnnouncementTagCrossRef
import com.kastik.database.entities.AnnouncementTagEntity
import com.kastik.database.views.AnnouncementDatabaseView

@Database(
    entities = [
        AnnouncementEntity::class,
        AnnouncementTagEntity::class,
        AnnouncementAttachmentEntity::class,
        AnnouncementAuthorEntity::class,
        AnnouncementTagCrossRef::class
    ],
    views = [
        AnnouncementPreviewDatabaseView::class,
        AnnouncementDatabaseView::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    StringListConverter::class
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
}