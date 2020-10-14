package com.app.demoapp.ui.noteslistmodule.noteListAdapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.demoapp.R
import com.app.demoapp.data.db.NotesEntity
import com.app.demoapp.ui.noteslistmodule.noteListAdapter.NotesAdapter.MyViewHolder
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter(context: Context, notesList: List<NotesEntity>) :
    RecyclerView.Adapter<MyViewHolder?>() {
    private val context: Context
    private val notesList: List<NotesEntity>

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var note: TextView
        var dot: TextView
        var timestamp: TextView
        var imgPin: ImageView

        init {
            note = view.findViewById(R.id.note)
            dot = view.findViewById(R.id.dot)
            timestamp = view.findViewById(R.id.timestamp)
            imgPin = view.findViewById(R.id.imgPin)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.note_list_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note: NotesEntity = notesList[position]
        holder.note.setText(note.title)

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"))

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(note.date))

        if (note.hasPin==0) {
            holder.imgPin.visibility = View.VISIBLE
        } else {
            holder.imgPin.visibility = View.GONE
        }
    }


    private fun formatDate(dateStr: String): String {
        try {
            val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date: Date = fmt.parse(dateStr)
            return date.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateStr
    }

    init {
        this.context = context
        this.notesList = notesList
    }

    override fun getItemCount(): Int {
        return notesList.size
    }
}