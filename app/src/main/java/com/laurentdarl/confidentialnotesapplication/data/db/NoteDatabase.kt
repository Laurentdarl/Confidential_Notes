package com.laurentdarl.confidentialnotesapplication.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.impl.WorkDatabaseMigrations.MIGRATION_1_2
import com.laurentdarl.confidentialnotesapplication.data.typeconverters.DateTypeConverter
import com.laurentdarl.confidentialnotesapplication.data.models.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDAO(): NoteDAO

    val MIGRATION_1_2: Migration = object: Migration(1,2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE note" + "ADD COLUMN date INTEGER DEFAULT NULL")
        }
    }
    companion object {
        @Volatile
        var INSTANCE: NoteDatabase? = null
            fun getNoteDatabase(context: Context): NoteDatabase {
                synchronized(this) {
                    return INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                        NoteDatabase::class.java, "note_db"
                    )
                        .addMigrations(MIGRATION_1_2)
                        .build()
                        .also {
                        INSTANCE = it
                    }
                }
            }
    }
}