package com.example.dalla

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration(private val spaceHeight : Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect){
            if (parent.getChildAdapterPosition(view) == 0) {
                left = spaceHeight
            }
            if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1)) {
                right = spaceHeight
            }
        }
    }
}