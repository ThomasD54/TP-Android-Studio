package com.dosne.minigames.data

class ScoreRepository(private val scoreDao: ScoreDao) {
    suspend fun insertScore(score: Score) {
        scoreDao.insertScore(score)
    }

    suspend fun getTopScores(): List<Score> {
        return scoreDao.getTopScores()
    }

    suspend fun getTopScoresByGame(gameName: String): List<Score> {
        return scoreDao.getTopScoresByGame(gameName)
    }
}
