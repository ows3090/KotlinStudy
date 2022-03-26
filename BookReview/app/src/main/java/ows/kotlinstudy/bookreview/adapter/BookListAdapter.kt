package ows.kotlinstudy.bookreview.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ows.kotlinstudy.bookreview.model.Book

class BookListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val asyncDiffer = AsyncListDiffer(this, DiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    fun changeList(books: List<Book>){
        asyncDiffer.submitList(books)
    }
}