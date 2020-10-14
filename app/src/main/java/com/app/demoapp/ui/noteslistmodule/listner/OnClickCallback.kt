package com.app.demoapp.ui.noteslistmodule.listner

import com.app.demoapp.data.db.NotesEntity

interface OnClickCallback {
    fun onClick(item: NotesEntity?)
}