package com.example.learningnew.contactsroom

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Contact::class],
    version = 5
)
@TypeConverters(
    LocalDateTimeConverter::class,
    GenderConverter::class
)
abstract class ContactDatabase: RoomDatabase() {
    abstract val dao: ContactDao
}

val MIGRATION_3_4 = object : Migration(2,3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE contact ADD COLUMN gender INTEGER DEFAULT 2")
    }
}