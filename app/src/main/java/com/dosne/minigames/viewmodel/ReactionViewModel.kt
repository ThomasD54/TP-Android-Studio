package com.dosne.minigames.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.abs

class ReactionViewModel : ViewModel() {
    // États pour la gestion du jeu
    var isRunning by mutableStateOf(false)
        private set
    
    var elapsedMs by mutableStateOf(15000L)
        private set
    
    var targetValue by mutableStateOf(5000L)
        private set
    
    var gamePhase by mutableStateOf("playing")
        private set
    
    var difference by mutableStateOf(0L)
        private set
    
    private var step by mutableStateOf(10L)
    
    private val random = java.util.Random()
    
    init {
        initializeGame()
    }
    
    /**
     * Initialise les paramètres aléatoires du jeu
     */
    private fun initializeGame() {
        val speed = 0.5 + random.nextDouble() * 1.0 // Entre 0.5 et 1.5
        val direction = if (random.nextBoolean()) 1 else -1 // 1 ou -1
        val startValue = random.nextLong(20000) // Entre 0 et 20000 ms
        val target = random.nextLong(8000) + 1000 // Entre 1000 et 9000 ms
        
        elapsedMs = startValue
        targetValue = target
        step = (10 * speed * direction).toLong()
        gamePhase = "playing"
        isRunning = false
    }
    
    /**
     * Démarre le timer
     */
    fun startTimer() {
        isRunning = true
    }
    
    /**
     * Arrête le timer et affiche le résultat
     */
    fun stopTimer() {
        isRunning = false
        difference = abs(elapsedMs - targetValue)
        gamePhase = "result"
    }
    
    /**
     * Incrémente le temps écoulé (appelé à chaque itération du timer)
     */
    fun updateTimer() {
        if (isRunning) {
            elapsedMs += step
            
            // Arrêter si le timer sort des limites
            if (elapsedMs < 0) {
                isRunning = false
                elapsedMs = 0
            }
        }
    }
    
    /**
     * Réinitialise le jeu pour une nouvelle partie
     */
    fun restartGame() {
        initializeGame()
    }
    
    /**
     * Retourne la couleur du timer en fonction de la proximité de la cible
     */
    fun getTimerColor(): Long {
        val gap = abs(elapsedMs - targetValue)
        return when {
            gap < 100 -> 0xFF4CAF50 // Vert
            gap < 300 -> 0xFFFFC107 // Orange
            else -> 0xFF2196F3 // Bleu
        }
    }
    
    /**
     * Retourne le message de feedback en fonction de l'écart
     */
    fun getFeedbackMessage(): String {
        return when {
            difference < 100 -> "Excellent!"
            difference < 300 -> "Bon!"
            difference < 500 -> "Pas mal"
            else -> "À recommencer"
        }
    }
    
    /**
     * Retourne la couleur du feedback en fonction de l'écart
     */
    fun getFeedbackColor(): Long {
        return when {
            difference < 100 -> 0xFF4CAF50 // Vert
            difference < 300 -> 0xFF2196F3 // Bleu
            difference < 500 -> 0xFFFFC107 // Orange
            else -> 0xFFFF5252 // Rouge
        }
    }
}
