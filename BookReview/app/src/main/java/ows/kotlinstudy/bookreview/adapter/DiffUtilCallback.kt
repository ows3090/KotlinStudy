package ows.kotlinstudy.bookreview.adapter

import androidx.recyclerview.widget.DiffUtil
import ows.kotlinstudy.bookreview.model.Book

class DiffUtilCallback(): DiffUtil.ItemCallback<Book>(){

    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}