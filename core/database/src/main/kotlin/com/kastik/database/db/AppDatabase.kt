package com.kastik.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kastik.database.dao.AnnouncementDao
import com.kastik.database.entities.AnnouncementAttachmentEntity
import com.kastik.database.entities.AnnouncementAuthorEntity
import com.kastik.database.entities.AnnouncementEntity
import com.kastik.database.entities.AnnouncementTagCrossRef
import com.kastik.database.entities.AnnouncementTagEntity

@Database(
    entities = [
        AnnouncementEntity::class,
        AnnouncementTagEntity::class,
        AnnouncementAttachmentEntity::class,
        AnnouncementAuthorEntity::class,
        AnnouncementTagCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
}