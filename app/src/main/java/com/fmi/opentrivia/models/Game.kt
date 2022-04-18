package com.fmi.opentrivia.models

import androidx.room.*

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "right answers") val rightAnswers: Int,
    @ColumnInfo(name = "wrong answers") val wrongAnswers: Int,
    @ColumnInfo(name = "category") val category: Int,
)


@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): List<Game>

    @Insert
    fun insert(game: Game)

    @Query("DELETE FROM game")
    fun nukeTable()
}
