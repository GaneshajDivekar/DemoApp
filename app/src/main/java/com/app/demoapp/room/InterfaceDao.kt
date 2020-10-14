package com.app.demoapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.demoapp.data.db.NotesEntity
import com.app.demoapp.room.dbHelper.DBUtils



@Dao
interface InterfaceDao {
    @Query("select * from notes ORDER BY pin ASC" )
    fun getNotes(): LiveData<List<NotesEntity>>
    @Insert
    fun insertNote(note: NotesEntity?): Long
    @Update
    fun updateNote(repos: NotesEntity?)
    @Delete
    fun deleteNote(note: NotesEntity?)

    @Query("UPDATE notes set pin ='0'  WHERE  id=:uniqueId")
    fun updatePin(uniqueId: Long)
}





