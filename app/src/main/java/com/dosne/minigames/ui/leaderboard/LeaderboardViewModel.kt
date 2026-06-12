package com.dosne.minigames.ui.leaderboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dosne.minigames.data.AppDatabase
import com.dosne.minigames.data.Score
import com.dosne.minigames.data.ScoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ScoreFilter(val label: String, val gameName: String?) {
    ALL("Tous", null),
    REACTION("Réaction", "Reaction"),
    WORD_GAME("Mot caché", "Mot caché")
}

class LeaderboardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ScoreRepository(
        AppDatabase.getDatabase(application).scoreDao()
    )

    private val _scores = MutableStateFlow<List<Score>>(emptyList())
    val scores: StateFlow<List<Score>> = _scores.asStateFlow()

    private val _filter = MutableStateFlow(ScoreFilter.ALL)
    val filter: StateFlow<ScoreFilter> = _filter.asStateFlow()

    init {
        loadScores()
    }

    fun setFilter(filter: ScoreFilter) {
        _filter.value = filter
        loadScores()
    }

    fun loadScores() {
        viewModelScope.launch {
            val selectedFilter = _filter.value
            _scores.value = selectedFilter.gameName?.let { gameName ->
                repository.getTopScoresByGame(gameName)
            } ?: repository.getTopScores()
        }
    }
}
