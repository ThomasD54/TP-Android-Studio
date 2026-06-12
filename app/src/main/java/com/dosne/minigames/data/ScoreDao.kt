package com.dosne.minigames.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDao {
    @Insert
    suspend fun insertScore(score: Score)

    @Query("SELECT * FROM scores ORDER BY score DESC, date ASC LIMIT 10")
    suspend fun getTopScores(): List<Score>

    @Query("SELECT * FROM scores WHERE game_name = :gameName ORDER BY score DESC, date ASC LIMIT 10")
    suspend fun getTopScoresByGame(gameName: String): List<Score>
}
