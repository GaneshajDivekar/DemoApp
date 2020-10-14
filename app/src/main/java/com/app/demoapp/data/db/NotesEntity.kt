package com.app.demoapp.data.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.demoapp.room.dbHelper.DBUtils
import java.util.*


@Entity(tableName = DBUtils.TABLE_NAME_NOTE)
class NotesEntity() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBUtils.NotesListConstant.COLUMN_ID)
    var note_id: Long = 0

    @ColumnInfo(name = DBUtils.NotesListConstant.COLUMN_TITLE) // column name will be "note_content" instead of "content" in table
    var title: String? = null



    @ColumnInfo(name = DBUtils.NotesListConstant.COLUMN_DESC) // column name will be "note_content" instead of "content" in table
    var desc: String? = null


    @ColumnInfo(name = DBUtils.NotesListConstant.COLUMN_TIMESTAMP) // column name will be "note_content" instead of "content" in table
    var date: String =  Date(System.currentTimeMillis()).toString()


    @ColumnInfo(name = DBUtils.NotesListConstant.COLUMN_PIN) // column name will be "note_content" instead of "content" in table
    var hasPin: Int? =1

    constructor(parcel: Parcel) : this() {
        note_id = parcel.readLong()
        title = parcel.readString()
        desc = parcel.readString()
        date = parcel.readString().toString()
        hasPin = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(note_id)
        parcel.writeString(title)
        parcel.writeString(desc)
        parcel.writeString(date)
        hasPin?.let { parcel.writeInt(it) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotesEntity> {
        override fun createFromParcel(parcel: Parcel): NotesEntity {
            return NotesEntity(parcel)
        }

        override fun newArray(size: Int): Array<NotesEntity?> {
            return arrayOfNulls(size)
        }
    }


}
