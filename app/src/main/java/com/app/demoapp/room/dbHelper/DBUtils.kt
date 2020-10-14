package com.app.demoapp.room.dbHelper

class DBUtils {
    companion object {
        const val DATABASE_NAME = "notesdb.db"
        const val DB_VERSION = 3
        const val TABLE_NAME_NOTE = "notes"


    }

    interface NotesListConstant {
        companion object {
            const val COLUMN_ID = "id"
            const val COLUMN_TITLE = "title"
            const val COLUMN_DESC = "desc"
            const val COLUMN_TIMESTAMP = "timestamp"
            const val COLUMN_PIN = "pin"
        }
    }


}
