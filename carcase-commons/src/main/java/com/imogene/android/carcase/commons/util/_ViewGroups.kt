package com.imogene.android.carcase.commons.util

import android.view.View
import android.view.ViewGroup

/**
 * Created by Admin on 04.11.2017.
 */

operator fun ViewGroup.plusAssign(view: View) = addView(view)

operator fun ViewGroup.minusAssign(view: View) = removeView(view)

operator fun ViewGroup.contains(view: View) = indexOfChild(view) != -1

operator fun ViewGroup.get(index: Int) : View? = getChildAt(index)

operator fun ViewGroup.set(index: Int, view: View) {
    removeViewAt(index)
    addView(view, index)
}

val ViewGroup.size get() = childCount

fun ViewGroup.children() = object : Iterable<View>{

    override fun iterator() = object : Iterator<View> {

        private var index = 0

        override fun hasNext() = index < size

        override fun next() = this@children[index++]!!
    }
}