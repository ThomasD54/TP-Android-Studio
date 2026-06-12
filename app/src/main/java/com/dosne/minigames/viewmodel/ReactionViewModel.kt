package com.dosne.minigames.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dosne.minigames.data.AppDatabase
import com.dosne.minigames.data.Score
import com.dosne.minigames.data.ScoreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random

enum class ReactionPhase {
    READY,
    PLAYING,
    RESULT
}

data class ReactionUiState(
    val phase: ReactionPhase = ReactionPhase.READY,
    val elapsedMs: Long = 0L,
    val targetMs: Long = 0L,
    val differenceMs: Long = 0L,
    val speedLabel: String = "",
    val isBlind: Boolean = false
) {
    val score: Int
        get() = (10_000 - differenceMs).coerceAtLeast(0).toInt()
}

class ReactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ScoreRepository(
        AppDatabase.getDatabase(application).scoreDao()
    )
    private val _uiState = MutableStateFlow(ReactionUiState())
    val uiState: StateFlow<ReactionUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var stepMs = 10L
    private var playerName = ""
    private var scoreSaved = false

    init {
        prepareRound()
    }

    fun startGame(playerName: String) {
        if (_uiState.value.phase == ReactionPhase.PLAYING) return

        this.playerName = playerName.trim()
        scoreSaved = false
        prepareRound()
        _uiState.update { it.copy(phase = ReactionPhase.PLAYING) }

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.phase == ReactionPhase.PLAYING) {
                delay(10L)
                tick()
            }
        }
    }

    fun stopTimer() {
        if (_uiState.value.phase != ReactionPhase.PLAYING) return

        timerJob?.cancel()
        val current = _uiState.value
        _uiState.update {
            it.copy(
                phase = ReactionPhase.RESULT,
                differenceMs = abs(current.elapsedMs - current.targetMs),
                isBlind = false
            )
        }
        saveScore()
    }

    fun reset() {
        timerJob?.cancel()
        scoreSaved = false
        prepareRound()
    }

    fun feedbackMessage(): String {
        val difference = _uiState.value.differenceMs
        return when {
            difference < 100 -> "Excellent !"
            difference < 300 -> "Très bon réflexe"
            difference < 700 -> "Pas mal"
            else -> "À retenter"
        }
    }

    private fun prepareRound() {
        val start = Random.nextLong(2_000L, 18_000L)
        val distance = Random.nextLong(2_500L, 7_000L)
        val goesUp = Random.nextBoolean()
        val target = if (goesUp) {
            (start + distance).coerceAtMost(25_000L)
        } else {
            (start - distance).coerceAtLeast(0L)
        }
        val speed = listOf(6L, 8L, 10L, 12L, 15L).random()

        stepMs = if (target >= start) speed else -speed
        _uiState.value = ReactionUiState(
            phase = ReactionPhase.READY,
            elapsedMs = start,
            targetMs = target,
            speedLabel = "Vitesse x${"%.1f".format(speed / 10.0)}"
        )
    }

    private fun tick() {
        _uiState.update { state ->
            val nextValue = (state.elapsedMs + stepMs).coerceIn(0L, 25_000L)
            val blind = abs(nextValue - state.targetMs) < 900L
            state.copy(elapsedMs = nextValue, isBlind = blind)
        }
    }

    private fun saveScore() {
        val currentPlayer = playerName.ifBlank { return }
        if (scoreSaved) return
        scoreSaved = true

        viewModelScope.launch {
            repository.insertScore(
                Score(
                    playerName = currentPlayer,
                    gameName = "Reaction",
                    score = _uiState.value.score
                )
            )
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}
