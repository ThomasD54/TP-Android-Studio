package com.dosne.minigames.ui.wordgame

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

enum class WordGamePhase {
    PLAYING,
    GAME_OVER
}

data class Cell(
    val char: Char,
    val isSelected: Boolean = false
)

data class WordGameUiState(
    val phase: WordGamePhase = WordGamePhase.PLAYING,
    val grid: List<Cell> = emptyList(),
    val selectedIndices: List<Int> = emptyList(),
    val selectedWord: String = "",
    val hiddenWordLength: Int = 6,
    val score: Int = 0,
    val timeLeft: Int = 60,
    val message: String = "",
    val hintUsed: Boolean = false,
    val hint: String = ""
)

class WordGameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ScoreRepository(
        AppDatabase.getDatabase(application).scoreDao()
    )
    private val words = listOf(
        "SOLEIL", "MAISON", "JARDIN", "CHEMIN", "BOUTON",
        "MIROIR", "PLANTE", "CARTON", "FUSEAU", "CITRON",
        "VIOLON", "RAPIDE", "BLOQUE", "MOUTON", "GATEAU",
        "CLAVIER", "ECRANS", "NUAGES", "ROCKET", "TABLES"
    ).map { it.take(6) }
    private val extraLetters = ('A'..'Z').toList()

    private val _uiState = MutableStateFlow(WordGameUiState())
    val uiState: StateFlow<WordGameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var currentWord = ""
    private var playerName = ""
    private var scoreSaved = false

    fun startGame(playerName: String) {
        val cleanPlayerName = playerName.trim()
        if (currentWord.isNotEmpty() && this.playerName == cleanPlayerName) {
            return
        }

        startNewGame(cleanPlayerName)
    }

    fun restartGame(playerName: String) {
        startNewGame(playerName.trim())
    }

    private fun startNewGame(playerName: String) {
        this.playerName = playerName.trim()
        scoreSaved = false
        timerJob?.cancel()
        loadNewRound(score = 0, timeLeft = 60, message = "")
        timerJob = viewModelScope.launch {
            while (_uiState.value.phase == WordGamePhase.PLAYING) {
                delay(1_000L)
                _uiState.update { state ->
                    val nextTime = state.timeLeft - 1
                    if (nextTime <= 0) {
                        state.copy(phase = WordGamePhase.GAME_OVER, timeLeft = 0)
                    } else {
                        state.copy(timeLeft = nextTime)
                    }
                }
                if (_uiState.value.phase == WordGamePhase.GAME_OVER) {
                    saveScore()
                }
            }
        }
    }

    fun selectCell(index: Int) {
        val state = _uiState.value
        if (state.phase != WordGamePhase.PLAYING) return
        val cell = state.grid.getOrNull(index) ?: return
        if (cell.isSelected) return

        _uiState.update {
            it.copy(
                grid = it.grid.mapIndexed { cellIndex, item ->
                    if (cellIndex == index) item.copy(isSelected = true) else item
                },
                selectedIndices = it.selectedIndices + index,
                selectedWord = it.selectedWord + cell.char,
                message = ""
            )
        }
    }

    fun eraseLast() {
        val state = _uiState.value
        val lastIndex = state.selectedIndices.lastOrNull() ?: return

        _uiState.update {
            it.copy(
                grid = it.grid.mapIndexed { index, item ->
                    if (index == lastIndex) item.copy(isSelected = false) else item
                },
                selectedIndices = it.selectedIndices.dropLast(1),
                selectedWord = it.selectedWord.dropLast(1),
                message = ""
            )
        }
    }

    fun validate() {
        val state = _uiState.value
        if (state.selectedWord == currentWord) {
            loadNewRound(
                score = state.score + 1,
                timeLeft = state.timeLeft,
                message = "Bravo !"
            )
        } else {
            _uiState.update { it.copy(message = "Ce n'est pas le bon mot") }
        }
    }

    fun pass() {
        val state = _uiState.value
        loadNewRound(
            score = state.score,
            timeLeft = state.timeLeft,
            message = "Nouvelle grille"
        )
    }

    fun revealHint() {
        val state = _uiState.value
        if (state.hintUsed || state.phase != WordGamePhase.PLAYING) return

        _uiState.update {
            it.copy(
                score = (it.score - 1).coerceAtLeast(0),
                hintUsed = true,
                hint = "Première lettre : ${currentWord.first()}"
            )
        }
    }

    fun reset() {
        restartGame(playerName)
    }

    private fun loadNewRound(score: Int, timeLeft: Int, message: String) {
        currentWord = words.random()
        val letters = (currentWord.toList() + List(3) { extraLetters.random() }).shuffled()
        _uiState.value = WordGameUiState(
            phase = WordGamePhase.PLAYING,
            grid = letters.map { Cell(it) },
            hiddenWordLength = currentWord.length,
            score = score,
            timeLeft = timeLeft,
            message = message
        )
    }

    private fun saveScore() {
        val currentPlayer = playerName.ifBlank { return }
        if (scoreSaved) return
        scoreSaved = true

        viewModelScope.launch {
            repository.insertScore(
                Score(
                    playerName = currentPlayer,
                    gameName = "Mot caché",
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
