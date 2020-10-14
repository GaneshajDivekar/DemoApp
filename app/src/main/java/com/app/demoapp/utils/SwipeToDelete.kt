/*
Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy of this
software and associated documentation files (the "Software"), to deal in the Software
without restriction, including without limitation the rights to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.app.demoapp.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.app.demoapp.R
import com.app.demoapp.ui.noteslistmodule.listner.RemoveCallback
import com.app.demoapp.ui.noteslistmodule.noteListAdapter.NotesAdapter.MyViewHolder

class SwipeToDelete internal constructor(
    context: Context,
    callback: RemoveCallback
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private val callback: RemoveCallback
    private val background: Drawable
    private val xMark: Drawable?
    private val xMarkMargin: Int
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //callback.onRemove((viewHolder as MyViewHolder).note)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (viewHolder != null) {
            // If the item has already been swiped away,ignore it
            if (viewHolder.adapterPosition == -1) return
            val vr = viewHolder.itemView.right
            val vt = viewHolder.itemView.top
            val vb = viewHolder.itemView.bottom
            val vh = vb - vt
            val iw = xMark!!.intrinsicWidth
            val ih = xMark.intrinsicHeight
            background.setBounds(vr + dX.toInt(), vt, vr, vb)
            background.draw(c)
            val xml = vr - xMarkMargin - iw
            val xmr = vr - xMarkMargin
            val xmt = vt + (vh - ih) / 2
            val xmb = xmt + ih
            xMark.setBounds(xml, xmt, xmr, xmb)
            xMark.draw(c)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    init {
        this.callback = callback
        background = ColorDrawable(Color.RED)
        xMark = ContextCompat.getDrawable(context, R.drawable.ic_clear_24dp)
        xMark!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        xMarkMargin = context.resources.getDimension(R.dimen.ic_clear_margin).toInt()
    }
}