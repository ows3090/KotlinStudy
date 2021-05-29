package ows.kotlinstudy.caculator

import androidx.room.Database
import androidx.room.RoomDatabase
import ows.kotlinstudy.caculator.dao.HistoryDao
import ows.kotlinstudy.caculator.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun historyDao() : HistoryDao
}