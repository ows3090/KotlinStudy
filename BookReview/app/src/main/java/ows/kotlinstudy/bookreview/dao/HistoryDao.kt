package ows.kotlinstudy.bookreview.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ows.kotlinstudy.bookreview.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM History")
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history WHERE keyword == :keyword ")
    fun delete(keyword: String)
}