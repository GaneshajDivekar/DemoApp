package com.app.demoapp.ui.noteslistmodule

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.demoapp.R
import com.app.demoapp.data.db.NotesEntity
import com.app.demoapp.databinding.FragmentNotesListBinding
import com.app.demoapp.presentation.base.BaseFragment
import com.app.demoapp.room.DatabaseHelper
import com.app.demoapp.ui.mainModule.MainActivity
import com.app.demoapp.ui.noteslistmodule.noteListAdapter.NotesAdapter
import com.app.demoapp.utils.MyDividerItemDecoration
import com.app.demoapp.utils.RecyclerTouchListener
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class NotesListFragment : BaseFragment<FragmentNotesListBinding, NotesListViewModel>(),
    NotesListNavigator {
    var fragmentNotesListBinding: FragmentNotesListBinding? = null
    val notesListViewModel: NotesListViewModel by viewModel()
    private var notesList = ArrayList<NotesEntity>()
    private var mAdapter: NotesAdapter? = null
    private val recyclerView: RecyclerView? = null


    private var db: DatabaseHelper? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_notes_list
    }

    override fun getViewModel(): NotesListViewModel {
        return notesListViewModel
    }

    override fun setUp(view: View, savedInstanceState: Bundle?) {
        fragmentNotesListBinding = getViewDataBinding()
        fragmentNotesListBinding!!.notesListViewModel = this
        (activity as MainActivity).toolbarTitle.setText("Notes List")

        db = DatabaseHelper.getDatabase(requireContext())

        mAdapter = NotesAdapter(requireContext(), notesList)
        val mLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext())
        fragmentNotesListBinding!!.recyclerView!!.layoutManager = mLayoutManager
        fragmentNotesListBinding!!.recyclerView!!.itemAnimator = DefaultItemAnimator()
        fragmentNotesListBinding!!.recyclerView.addItemDecoration(
            MyDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                16
            )
        )
        fragmentNotesListBinding!!.recyclerView.adapter = mAdapter



        db?.interfaceDao()?.getNotes()?.observe(this, androidx.lifecycle.Observer {
            notesList.clear()
            notesList.addAll(it)
            mAdapter!!.notifyDataSetChanged()
        })



        toggleEmptyNotes()

        fragmentNotesListBinding!!.recyclerView.addOnItemTouchListener(
            RecyclerTouchListener(requireContext(),
                fragmentNotesListBinding!!.recyclerView,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {}
                    override fun onLongClick(view: View?, position: Int) {
                        showActionsDialog(position)
                    }
                })
        )
        fragmentNotesListBinding!!.fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                // view.findNavController().navigate(R.id.action_notesListFragmet_to_addNoteFragment)
                showNoteDialog(false, null, -1)

            }

        })
    }

    private fun showNoteDialog(
        shouldUpdate: Boolean,
        note: NotesEntity?,
        position: Int
    ) {
        val layoutInflaterAndroid = LayoutInflater.from(requireContext())
        val view: View = layoutInflaterAndroid.inflate(R.layout.note_dialog, null)
        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(requireContext())
        alertDialogBuilderUserInput.setView(view)
        val inputNoteTitle = view.findViewById<EditText>(R.id.noteTitle)
        val inputNoteDesc = view.findViewById<EditText>(R.id.noteDesc)
        val dialogTitle = view.findViewById<TextView>(R.id.dialog_title)
        dialogTitle.text = if (!shouldUpdate) getString(R.string.lbl_new_note_title) else getString(
            R.string.lbl_edit_note_title
        )
        if (shouldUpdate && note != null) {
            inputNoteTitle.setText(note.title)
            inputNoteDesc.setText(note.desc)
        }
        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(
                if (shouldUpdate) "update" else "save"
            ) { dialogBox, id -> }
            .setNegativeButton(
                "cancel"
            ) { dialogBox, id -> dialogBox.cancel() }
        val alertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener(View.OnClickListener {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNoteTitle.text.toString())) {
                    Toast.makeText(requireContext(), "Enter Title!", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                } else {
                    alertDialog.dismiss()
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // update note by it's id
                    updateNote(
                        inputNoteTitle.text.toString(),
                        inputNoteDesc.text.toString(),
                        position
                    )
                } else {
                    // create new note
                    createNote(inputNoteTitle.text.toString(), inputNoteDesc.text.toString())
                }
            })
    }

    private fun createNote(noteTitle: String, noteDesc: String) {


        var notesEntity = NotesEntity()
        notesEntity.title = noteTitle
        notesEntity.desc = noteDesc
        notesEntity.hasPin = 1
        db!!.interfaceDao().insertNote(notesEntity)
        toggleEmptyNotes()

    }

    private fun updateNote(noteTitle: String, noteDesc: String, position: Int) {
        val n: NotesEntity = notesList[position]
        // updating note text
        // updating note text
        n.title = (noteTitle)
        n.desc = noteDesc

        // updating note in db

        // updating note in db
        db?.interfaceDao()?.updateNote(n)

        // refreshing the list

        // refreshing the list
        notesList[position] = n
        mAdapter!!.notifyItemChanged(position)

        toggleEmptyNotes()
    }

    private fun showActionsDialog(position: Int) {
        val colors =
            arrayOf<CharSequence>("Edit", "Delete", "Pin")

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose option")
        builder.setItems(colors, DialogInterface.OnClickListener { dialog, which ->
            if (which == 0) {
                showNoteDialog(true, notesList[position], position)
            } else if (which == 1) {
                deleteNote(position)
            } else {
                pinChat(position)

            }
        })
        builder.show()
    }

    private fun pinChat(position: Int) {
        val item = notesList.get(position)
        val itemPos: Int = notesList.indexOf(item)
        notesList.removeAt(itemPos)
        notesList.add(0, item)
        db?.interfaceDao()?.updatePin(item.note_id)

        toggleEmptyNotes()
    }

    private fun deleteNote(position: Int) {
        db?.interfaceDao()!!.deleteNote(notesList[position])
        notesList.remove(notesList[position])
        mAdapter!!.notifyItemRemoved(position)
        toggleEmptyNotes()

    }


    private fun toggleEmptyNotes() {
        db?.interfaceDao()?.getNotes()?.observe(this, androidx.lifecycle.Observer {
            notesList.clear()
            notesList.addAll(it)

            if (notesList.size>0) {
                fragmentNotesListBinding!!.emptyNotesView!!.visibility = View.GONE
            } else {
                fragmentNotesListBinding!!.emptyNotesView!!.visibility = View.VISIBLE
            }
            mAdapter!!.notifyDataSetChanged()
        })



    }
}