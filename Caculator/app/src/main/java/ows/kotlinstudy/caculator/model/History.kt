package ows.kotlinstudy.caculator.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo(name = "expresiion") val expression : String?,
    @ColumnInfo(name = "result") val result : String?
){
    constructor(expression: String?, result: String?):this(0,expression, result)
}